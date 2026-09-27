package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModBlocks;
import com.asestefan.mutationcraft.init.ModParticles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class FlameSpray {
    private static final double BASE_RANGE = 8.0;
    public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ModUtil.id("mutationcraft:flamethrower"));
    private static final double CONE_DEGREES = 24.0;
    private static final double CONE_COS = Math.cos(Math.toRadians(CONE_DEGREES));
    private static final int RING_RAYS = 8;
    private static final double[] RINGS = {0.5, 1.0};
    private static final double STEP = 0.5;
    private static final double FLAME_SPEED = 0.8;
    private static final Map<ServerLevel, List<Burst>> BURSTS = new WeakHashMap<>();
    private static final double PLAYER_TIP_FORWARD = 1.6;
    private static final double PLAYER_TIP_SIDE = 0.37;
    private static final double PLAYER_TIP_UP = -0.1;

    public static double range() {
        return MutationcraftConfig.FLAMETHROWER_RANGE.get();
    }

    public static Vec3 tip(LivingEntity shooter, Vec3 direction) {
        Vec3 side = direction.cross(new Vec3(0.0, 1.0, 0.0));
        side = side.lengthSqr() < 1.0E-4 ? new Vec3(1.0, 0.0, 0.0) : side.normalize();
        double hand = shooter.getMainArm() == HumanoidArm.RIGHT ? 1.0 : -1.0;
        Vec3 up = side.cross(direction).normalize();
        return shooter.getEyePosition().add(direction.scale(PLAYER_TIP_FORWARD)).add(side.scale(PLAYER_TIP_SIDE * hand)).add(up.scale(PLAYER_TIP_UP));
    }

    public static boolean submerged(LivingEntity shooter) {
        return shooter.isEyeInFluid(FluidTags.WATER);
    }

    public static void particles(ServerLevel level, LivingEntity shooter, Vec3 origin, Vec3 direction) {
        RandomSource random = level.getRandom();
        double scale = range() / BASE_RANGE;
        if (submerged(shooter)) {
            level.sendParticles(ParticleTypes.BUBBLE, origin.x, origin.y, origin.z, 8, 0.2, 0.2, 0.2, 0.08);
            return;
        }
        for (int i = 0; i < 12; i++) {
            Vec3 velocity = spread(direction, random, CONE_DEGREES * 0.8).scale((0.6 + random.nextDouble() * 0.45) * scale);
            level.sendParticles(ModParticles.FLAMETHROWER_FLAME.get(), origin.x, origin.y, origin.z, 0, velocity.x, velocity.y, velocity.z, 1.0);
        }
        for (int i = 0; i < 4; i++) {
            Vec3 velocity = spread(direction, random, CONE_DEGREES).scale((0.2 + random.nextDouble() * 0.35) * scale);
            level.sendParticles(ParticleTypes.LARGE_SMOKE, origin.x, origin.y, origin.z, 0, velocity.x, velocity.y + 0.04, velocity.z, 1.0);
        }
        if (random.nextInt(3) == 0) {
            level.sendParticles(ParticleTypes.LARGE_SMOKE, origin.x, origin.y + 0.1, origin.z, 2, 0.1, 0.1, 0.1, 0.02);
        }
        if (random.nextInt(6) == 0) {
            level.sendParticles(ParticleTypes.LAVA, origin.x, origin.y, origin.z, 1, 0.1, 0.1, 0.1, 0.0);
        }
    }

    public static void burn(ServerLevel level, LivingEntity shooter, Vec3 origin, Vec3 direction, float damage, ItemStack stack) {
        burn(level, shooter, origin, direction, damage, 1.0, stack);
    }

    public static void burn(ServerLevel level, LivingEntity shooter, Vec3 origin, Vec3 direction, float damage, double igniteChance, ItemStack stack) {
        if (submerged(shooter)) {
            return;
        }
        boolean griefing = shooter instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        double range = range();
        Vec3[] rays = rays(direction);
        BlockHitResult[] hits = new BlockHitResult[rays.length];
        double[] reach = new double[rays.length];
        for (int i = 0; i < rays.length; i++) {
            BlockHitResult hit = level.clip(new ClipContext(origin, origin.add(rays[i].scale(range)), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, shooter));
            hits[i] = hit;
            reach[i] = hit.getType() == HitResult.Type.MISS ? range : origin.distanceTo(hit.getLocation());
        }
        BURSTS.computeIfAbsent(level, key -> new ArrayList<>()).add(new Burst(shooter, origin, direction, rays, hits, reach, range, damage, igniteChance, griefing, stack.copy()));
    }

    public static void tick(ServerLevel level) {
        List<Burst> bursts = BURSTS.get(level);
        if (bursts == null || bursts.isEmpty()) {
            return;
        }
        Iterator<Burst> iterator = bursts.iterator();
        while (iterator.hasNext()) {
            Burst burst = iterator.next();
            if (burst.advance(level)) {
                iterator.remove();
            }
        }
    }

    public static boolean allyInStream(LivingEntity shooter, Vec3 origin, Vec3 direction, double reach) {
        AABB box = new AABB(origin, origin.add(direction.scale(reach))).inflate(3.0);
        return !shooter.level().getEntitiesOfClass(LivingEntity.class, box,
                entity -> entity != shooter && entity.isAlive() && shooter.isAlliedTo(entity) && entity.distanceTo(shooter) <= reach && inStream(entity, origin, direction, range())).isEmpty();
    }

    private static final class Burst {
        private final LivingEntity shooter;
        private final Player player;
        private final Vec3 origin;
        private final Vec3 direction;
        private final Vec3[] rays;
        private final BlockHitResult[] hits;
        private final double[] reach;
        private final double range;
        private final float damage;
        private final double igniteChance;
        private final boolean griefing;
        private final ItemStack stack;
        private final Set<LivingEntity> burned = new HashSet<>();
        private double travelled;

        private Burst(LivingEntity shooter, Vec3 origin, Vec3 direction, Vec3[] rays, BlockHitResult[] hits, double[] reach, double range, float damage, double igniteChance, boolean griefing, ItemStack stack) {
            this.shooter = shooter;
            this.player = shooter instanceof Player p ? p : null;
            this.origin = origin;
            this.direction = direction;
            this.rays = rays;
            this.hits = hits;
            this.reach = reach;
            this.range = range;
            this.damage = damage;
            this.igniteChance = igniteChance;
            this.griefing = griefing;
            this.stack = stack;
        }

        private boolean advance(ServerLevel level) {
            double from = this.travelled;
            this.travelled = Math.min(this.range, from + FLAME_SPEED);
            boolean foliage = this.player == null || MutationcraftConfig.FLAMETHROWER_BURNS_FOLIAGE.get();
            boolean fires = this.player == null || MutationcraftConfig.FLAMETHROWER_LIGHTS_FIRES.get();
            for (int i = 0; this.griefing && i < this.rays.length; i++) {
                double end = Math.min(this.travelled, this.reach[i]);
                if (foliage && end > from) {
                    clearFoliage(level, this.shooter, this.player, this.origin, this.rays[i], from, end, this.stack);
                }
                BlockHitResult hit = this.hits[i];
                if (hit.getType() == HitResult.Type.BLOCK && this.reach[i] > from && this.reach[i] <= this.travelled
                        && (this.player == null || this.player.mayUseItemAt(hit.getBlockPos(), hit.getDirection(), this.stack))) {
                    if (!heat(level, hit.getBlockPos(), level.getBlockState(hit.getBlockPos())) && fires
                            && (this.igniteChance >= 1.0 || level.getRandom().nextDouble() < this.igniteChance)) {
                        ignite(level, this.player, hit, this.stack);
                    }
                }
            }
            burnEntities(level, from);
            return this.travelled >= this.range;
        }

        private void burnEntities(ServerLevel level, double from) {
            double radius = this.travelled * Math.tan(Math.toRadians(CONE_DEGREES)) + 1.0;
            Vec3 near = this.origin.add(this.direction.scale(from));
            Vec3 far = this.origin.add(this.direction.scale(this.travelled));
            AABB box = new AABB(near, far).inflate(radius);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, entity -> entity != this.shooter && entity.isAlive() && !entity.isSpectator())) {
                if (this.burned.contains(target) || this.shooter.isAlliedTo(target)) {
                    continue;
                }
                double along = target.getBoundingBox().getCenter().subtract(this.origin).dot(this.direction);
                if (along > this.travelled + 0.5 || !inStream(target, this.origin, this.direction, this.range)) {
                    continue;
                }
                if (target instanceof Player targetPlayer && targetPlayer.getAbilities().invulnerable) {
                    continue;
                }
                this.burned.add(target);
                int burnSeconds = MutationcraftConfig.FLAMETHROWER_BURN_SECONDS.getInt();
                if (!target.fireImmune() && burnSeconds > 0) {
                    ModUtil.setOnFire(target, burnSeconds);
                }
                if (this.player != null) {
                    target.setLastHurtByPlayer(this.player);
                } else {
                    target.setLastHurtByMob(this.shooter);
                }
                target.invulnerableTime = 0;
                target.hurt(damageSource(level, this.shooter), this.damage);
            }
        }
    }

    private static DamageSource damageSource(ServerLevel level, LivingEntity shooter) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DAMAGE_TYPE), shooter);
    }

    private static Vec3[] rays(Vec3 direction) {
        Vec3 side = direction.cross(new Vec3(0.0, 1.0, 0.0));
        side = side.lengthSqr() < 1.0E-4 ? new Vec3(1.0, 0.0, 0.0) : side.normalize();
        Vec3 up = side.cross(direction).normalize();
        Vec3[] rays = new Vec3[1 + RINGS.length * RING_RAYS];
        rays[0] = direction;
        int index = 1;
        for (int ring = 0; ring < RINGS.length; ring++) {
            double tilt = Math.toRadians(CONE_DEGREES * RINGS[ring]);
            double offset = ring % 2 == 0 ? 0.0 : Math.PI / RING_RAYS;
            for (int i = 0; i < RING_RAYS; i++) {
                double angle = offset + i * Math.PI * 2.0 / RING_RAYS;
                rays[index++] = direction.scale(Math.cos(tilt))
                        .add(side.scale(Math.cos(angle) * Math.sin(tilt)))
                        .add(up.scale(Math.sin(angle) * Math.sin(tilt)))
                        .normalize();
            }
        }
        return rays;
    }

    private static Vec3 spread(Vec3 direction, RandomSource random, double degrees) {
        double max = Math.toRadians(degrees);
        Vec3 side = direction.cross(new Vec3(0.0, 1.0, 0.0));
        side = side.lengthSqr() < 1.0E-4 ? new Vec3(1.0, 0.0, 0.0) : side.normalize();
        Vec3 up = side.cross(direction).normalize();
        double angle = random.nextDouble() * Math.PI * 2.0;
        double tilt = Math.sqrt(random.nextDouble()) * max;
        double sin = Mth.sin((float) tilt);
        return direction.scale(Math.cos(tilt)).add(side.scale(Math.cos(angle) * sin)).add(up.scale(Math.sin(angle) * sin)).normalize();
    }

    private static boolean inStream(LivingEntity target, Vec3 origin, Vec3 direction, double range) {
        AABB bounds = target.getBoundingBox().inflate(0.3);
        Vec3 center = bounds.getCenter();
        Vec3 offset = center.subtract(origin);
        if (offset.length() > range + 1.0) {
            return false;
        }
        boolean aimed = bounds.clip(origin, origin.add(direction.scale(range))).isPresent() || offset.normalize().dot(direction) >= CONE_COS;
        if (!aimed) {
            return false;
        }
        return target.level().clip(new ClipContext(origin, center, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target)).getType() == HitResult.Type.MISS;
    }

    private static boolean heat(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE) || state.is(Blocks.FROSTED_ICE)) {
            if (level.dimensionType().ultraWarm()) {
                level.removeBlock(pos, false);
            } else {
                level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }
        } else if (state.is(Blocks.WET_SPONGE)) {
            level.setBlockAndUpdate(pos, Blocks.SPONGE.defaultBlockState());
        } else {
            return false;
        }
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 1.6F + level.getRandom().nextFloat() * 0.4F);
        level.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 6, 0.3, 0.2, 0.3, 0.02);
        return true;
    }

    private static void clearFoliage(ServerLevel level, LivingEntity shooter, Player player, Vec3 origin, Vec3 ray, double from, double to, ItemStack stack) {
        BlockPos last = null;
        for (double d = Math.max(0.5, from); d <= to + 0.5; d += STEP) {
            BlockPos pos = BlockPos.containing(origin.add(ray.scale(d)));
            if (pos.equals(last)) {
                continue;
            }
            last = pos;
            BlockState state = level.getBlockState(pos);
            if (isFoliage(state) && (player == null || player.mayUseItemAt(pos, Direction.UP, stack))) {
                level.destroyBlock(pos, false, shooter);
            }
        }
    }

    private static boolean isFoliage(BlockState state) {
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        return state.is(BlockTags.LEAVES) || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.SAPLINGS)
                || state.is(ModBlocks.PUTRID_VINE.get()) || state.is(Blocks.COBWEB) || state.is(Blocks.SNOW) || state.is(Blocks.SWEET_BERRY_BUSH);
    }

    private static void ignite(ServerLevel level, Player player, BlockHitResult hit, ItemStack stack) {
        BlockPos[] candidates = {hit.getBlockPos().relative(hit.getDirection()), hit.getBlockPos().above()};
        for (BlockPos pos : candidates) {
            if (!level.isEmptyBlock(pos) || player != null && !player.mayUseItemAt(pos, hit.getDirection(), stack)) {
                continue;
            }
            BlockState fire = BaseFireBlock.getState(level, pos);
            if (fire.canSurvive(level, pos)) {
                level.setBlock(pos, fire, 11);
                return;
            }
        }
    }

    private FlameSpray() {
    }
}
