package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.AssimilatedEvokerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedVexEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.GameType;

public final class AssimilatedEvokerBehavior {
    private static final double VEX_COUNT_RANGE = 16.0;
    private static final double[][] FANG_OFFSETS = {{0.0, 0.0}, {1.0, -1.0}, {2.0, -2.0}, {-1.0, 1.0}, {-2.0, 2.0}};

    public static void attack(LivingEntity target) {
        target.level().playSound(null, target.blockPosition(), ModSounds.MUTANT_BITE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        target.addEffect(new MobEffectInstance(ModMobEffects.CORROSION.ref(), 200, 0));
    }

    public static void retaliate(AssimilatedEvokerEntity evoker, Player player) {
        if (!(evoker.level() instanceof ServerLevel level)) {
            return;
        }
        double x = evoker.getX();
        double y = evoker.getY();
        double z = evoker.getZ();
        RandomSource random = evoker.getRandom();
        MutationcraftMod.queueServerWork(level, 30, () -> {
            if (random.nextDouble() <= 0.45) {
                evoker.playAnimation("summon1");
                MutationcraftMod.queueServerWork(level, 21, () -> fangs(level, player, x, y, z));
            }
        });
        MutationcraftMod.queueServerWork(level, 60, () -> {
            if (random.nextDouble() <= 0.2) {
                evoker.playAnimation("summon2");
                MutationcraftMod.queueServerWork(level, 22, () -> vexes(level, evoker, player, y, z));
            }
            if (random.nextDouble() <= 0.1 && vulnerable(player) && player.level() == level
                    && ModUtil.teleportNear(player, evoker.getX() + 2.0, y, evoker.getZ())) {
                particles(level, ParticleTypes.POOF, player.getX(), y, z, 3, 0.5, 1.5, 0.5);
                particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 3, 0.7, 1.6, 0.7);
                particles(level, ParticleTypes.FLASH, player.getX(), y, z, 2, 0.8, 1.7, 1.2);
            }
        });
    }

    private static void fangs(ServerLevel level, Player player, double x, double y, double z) {
        if (!vulnerable(player) || player.level() != level) {
            return;
        }
        double fangY = player.getY();
        for (double[] offset : FANG_OFFSETS) {
            EvokerFangs fang = new EvokerFangs(EntityType.EVOKER_FANGS, level);
            fang.moveTo(player.getX() + offset[0], fangY, player.getZ() + offset[1], level.getRandom().nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(fang);
        }
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 4, 0.6, 1.4, 1.2);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 7, 0.9, 1.4, 0.8);
        particles(level, ParticleTypes.FLASH, player.getX(), y, z, 2, 0.4, 1.5, 0.9);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 2, 0.5, 1.7, 0.7);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 3, 0.9, 1.7, 1.1);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 3, 0.7, 1.6, 0.7);
        level.playSound(null, BlockPos.containing(x, y, z), ModSounds.ASSIMILATED_EVOKER_SUMMON_FANGS.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private static void vexes(ServerLevel level, AssimilatedEvokerEntity evoker, Player player, double y, double z) {
        level.playSound(null, BlockPos.containing(evoker.getX(), y, z), ModSounds.ASSIMILATED_EVOKER_SUMMON_VEX.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        int count = level.getEntitiesOfClass(AssimilatedVexEntity.class, evoker.getBoundingBox().inflate(VEX_COUNT_RANGE), e -> true).size();
        for (double offset : new double[]{0.5, -0.5}) {
            if (count >= MutationcraftConfig.EVOKER_VEX_LIMIT.getInt()) {
                break;
            }
            AssimilatedVexEntity vex = ModEntities.ASSIMILATED_VEX.get().create(level);
            if (vex == null) {
                continue;
            }
            vex.moveTo(evoker.getX(), y + 0.5, z + offset, level.getRandom().nextFloat() * 360.0F, 0.0F);
            ModUtil.finalizeSpawn(vex, level, level.getCurrentDifficultyAt(vex.blockPosition()), MobSpawnType.MOB_SUMMONED);
            vex.setLimitedLife(20 * (30 + level.getRandom().nextInt(90)));
            level.addFreshEntity(vex);
            count++;
        }
        particles(level, ParticleTypes.EXPLOSION, evoker.getX(), y, z, 4, 1.0, 3.0, 1.0);
        particles(level, ParticleTypes.FLASH, player.getX(), y, z, 2, 0.4, 1.5, 0.9);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 3, 0.6, 1.7, 0.8);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 2, 0.5, 1.7, 0.7);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 3, 0.7, 1.6, 0.7);
        particles(level, ParticleTypes.FIREWORK, player.getX(), y, z, 2, 0.8, 1.7, 1.2);
    }

    private static boolean vulnerable(Player player) {
        return ModUtil.isGameMode(player, GameType.SURVIVAL) || ModUtil.isGameMode(player, GameType.ADVENTURE);
    }

    private static void particles(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double dx, double dy, double dz) {
        level.sendParticles(particle, x, y, z, count, dx, dy, dz, 1.0);
    }

    private AssimilatedEvokerBehavior() {
    }
}
