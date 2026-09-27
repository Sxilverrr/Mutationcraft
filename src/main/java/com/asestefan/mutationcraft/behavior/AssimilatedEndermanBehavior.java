package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.AssimilatedEndermanEntity;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.asestefan.mutationcraft.entity.HumanHerderEntity;
import com.asestefan.mutationcraft.entity.ResenterEntity;
import com.asestefan.mutationcraft.entity.TheIntoxicatorEntity;
import com.asestefan.mutationcraft.init.ModSounds;
import com.asestefan.mutationcraft.network.ModVariables;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public final class AssimilatedEndermanBehavior {
    private static final double PLAYER_RANGE = 32.0;
    private static final double ALLY_RANGE = 48.0;
    private static final float STRONG_HEALTH = 20.0F;
    private static final float[] STAGE_MAX_HEALTH = {20.0F, 35.0F, 50.0F, 75.0F, Float.MAX_VALUE};
    private static final double ALLY_MIN_DISTANCE = 5.0;
    private static final double HEAL_RANGE = 10.0;
    private static final double HURT_BLINK_CHANCE = 0.13;
    private static final int BLINK_COOLDOWN_MIN = 300;
    private static final int BLINK_COOLDOWN_RANGE = 300;
    private static final int GRAB_COOLDOWN_MIN = 400;
    private static final int GRAB_COOLDOWN_RANGE = 100;
    private static final int GRAB_HOLD_TICKS = 6;
    private static final int GRAB_RETRY_MIN = 100;
    private static final int GRAB_RETRY_RANGE = 40;
    private static final int GRAB_RETRIES = 5;
    private static final int REGRAB_COOLDOWN_MIN = 600;
    private static final int REGRAB_COOLDOWN_RANGE = 600;
    private static final String BLINK_KEY = "mutationcraft:blink_ready_at";
    private static final String GRAB_KEY = "mutationcraft:grab_ready_at";
    private static final String REGRAB_KEY = "mutationcraft:regrab_ready_at";

    public static void tick(AssimilatedEndermanEntity enderman) {
        if (!(enderman.level() instanceof ServerLevel level) || !enderman.isAlive() || enderman.tickCount % 20 != 0) {
            return;
        }
        long now = level.getGameTime();
        boolean canGrab = MutationcraftConfig.ENDERMAN_BRINGS_MUTANTS.get() && ready(enderman, GRAB_KEY, now);
        boolean canBlink = MutationcraftConfig.ENDERMAN_TELEPORTS_TO_YOU.get() && ready(enderman, BLINK_KEY, now);
        if (!canGrab && !canBlink) {
            return;
        }
        Player player = victim(enderman, level);
        if (player == null) {
            return;
        }
        if (canGrab) {
            Mob ally = findAlly(enderman, player, level);
            if (ally != null) {
                setCooldown(enderman, GRAB_KEY, now, GRAB_COOLDOWN_MIN, GRAB_COOLDOWN_RANGE);
                setCooldown(enderman, BLINK_KEY, now, BLINK_COOLDOWN_MIN, BLINK_COOLDOWN_RANGE);
                grab(enderman, ally, player, level);
                return;
            }
            setCooldown(enderman, GRAB_KEY, now, GRAB_RETRY_MIN, GRAB_RETRY_RANGE);
        }
        if (canBlink && enderman.distanceToSqr(player) > 36.0) {
            setCooldown(enderman, BLINK_KEY, now, BLINK_COOLDOWN_MIN, BLINK_COOLDOWN_RANGE);
            warp(enderman, player.getX() + 1.0, player.getY(), player.getZ());
        }
    }

    public static void attack(AssimilatedEndermanEntity enderman, LivingEntity target) {
        if (enderman.getRandom().nextDouble() > 0.2) {
            return;
        }
        enderman.playAnimation("attack");
        MutationcraftMod.queueServerWork(enderman.level(), 15, () -> {
            if (target.isAlive()) {
                target.hurt(target.damageSources().generic(), 6.0F);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
            }
        });
    }

    public static void hurt(AssimilatedEndermanEntity enderman, DamageSource source) {
        if (enderman.level().isClientSide() || source.getEntity() == null) {
            return;
        }
        RandomSource random = enderman.getRandom();
        if (random.nextDouble() < HURT_BLINK_CHANCE) {
            warp(enderman, enderman.getX() + random.nextInt(11) - 5, enderman.getY(), enderman.getZ() + random.nextInt(11) - 5);
        }
    }

    public static void onPlayerKilled(Player player, AssimilatedEndermanEntity killer) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        Map<EntityType<?>, LivingEntity> nearestByType = new HashMap<>();
        for (LivingEntity mutant : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(HEAL_RANGE), ModUtil::isMutant)) {
            nearestByType.merge(mutant.getType(), mutant, (a, b) -> a.distanceToSqr(player) <= b.distanceToSqr(player) ? a : b);
        }
        boolean healed = false;
        for (LivingEntity mutant : nearestByType.values()) {
            if (mutant.getHealth() < mutant.getMaxHealth()) {
                mutant.setHealth(mutant.getMaxHealth());
                healed = true;
            }
        }
        if (healed) {
            level.sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.getX(), player.getY(), player.getZ(), 1, 1.0, 3.0, 1.0, 1.0);
            level.playSound(null, player.blockPosition(), ModSounds.MUTANT_BITE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            killer.playAnimation("kill");
        }
    }

    public static boolean warp(Entity entity, double x, double y, double z) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return false;
        }
        double fromX = entity.getX();
        double fromY = entity.getY();
        double fromZ = entity.getZ();
        if (!ModUtil.teleportNear(entity, x, y, z)) {
            return false;
        }
        warpEffects(level, entity, fromX, fromY, fromZ, ModSounds.ASSIMILATED_ENDERMAN_TELEPORT_FAR.get());
        warpEffects(level, entity, entity.getX(), entity.getY(), entity.getZ(), ModSounds.ASSIMILATED_ENDERMAN_TELEPORT.get());
        return true;
    }

    private static void grab(AssimilatedEndermanEntity enderman, Mob ally, Player player, ServerLevel level) {
        if (!warp(enderman, ally.getX() + 1.0, ally.getY(), ally.getZ())) {
            return;
        }
        enderman.getNavigation().stop();
        ally.getNavigation().stop();
        ModUtil.data(ally).putLong(REGRAB_KEY, level.getGameTime() + REGRAB_COOLDOWN_MIN + enderman.getRandom().nextInt(REGRAB_COOLDOWN_RANGE));
        MutationcraftMod.queueServerWork(level, GRAB_HOLD_TICKS, () -> bringBack(enderman, ally, player, level, GRAB_RETRIES));
    }

    private static void bringBack(AssimilatedEndermanEntity enderman, Mob ally, Player player, ServerLevel level, int retries) {
        if (!enderman.isAlive() || !player.isAlive() || player.level() != level) {
            return;
        }
        boolean returned = warp(enderman, player.getX() + 2.0, player.getY(), player.getZ()) || warp(enderman, player.getX(), player.getY(), player.getZ());
        if (!returned) {
            if (retries > 0) {
                MutationcraftMod.queueServerWork(level, 2, () -> bringBack(enderman, ally, player, level, retries - 1));
            }
            return;
        }
        enderman.setTarget(player);
        if (ally.isAlive() && ally.level() == level
                && (warp(ally, enderman.getX() + 1.0, enderman.getY(), enderman.getZ() + 1.0) || warp(ally, player.getX(), player.getY(), player.getZ()))) {
            ally.setTarget(player);
        }
    }

    private static Player victim(AssimilatedEndermanEntity enderman, ServerLevel level) {
        if (enderman.getTarget() instanceof Player target && valid(target) && enderman.distanceToSqr(target) <= PLAYER_RANGE * PLAYER_RANGE) {
            return target;
        }
        return level.getEntitiesOfClass(Player.class, enderman.getBoundingBox().inflate(PLAYER_RANGE), AssimilatedEndermanBehavior::valid)
                .stream()
                .min(Comparator.comparingDouble(enderman::distanceToSqr))
                .orElse(null);
    }

    private static boolean valid(Player player) {
        return player.isAlive() && !player.isCreative() && !player.isSpectator();
    }

    private static Mob findAlly(AssimilatedEndermanEntity enderman, Player player, ServerLevel level) {
        float maxHealth = STAGE_MAX_HEALTH[Math.max(0, Math.min(STAGE_MAX_HEALTH.length - 1, ModVariables.stage(level)))];
        long now = level.getGameTime();
        List<Mob> candidates = level.getEntitiesOfClass(Mob.class, enderman.getBoundingBox().inflate(ALLY_RANGE),
                mob -> mob.getMaxHealth() <= maxHealth && ready(mob, REGRAB_KEY, now) && grabbable(enderman, mob, player));
        Comparator<Mob> nearest = Comparator.comparingDouble(enderman::distanceToSqr);
        Mob strongest = candidates.stream()
                .filter(mob -> mob.getMaxHealth() >= STRONG_HEALTH)
                .max(Comparator.comparingDouble(Mob::getMaxHealth).thenComparing(nearest.reversed()))
                .orElse(null);
        return strongest != null ? strongest : candidates.stream().min(nearest).orElse(null);
    }

    private static boolean grabbable(AssimilatedEndermanEntity enderman, Mob mob, Player player) {
        return mob != enderman && mob.isAlive() && ModUtil.isMutant(mob) && !(mob instanceof AssimilatedEndermanEntity) && !isBoss(mob)
                && mob.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0.0 && !mob.isPassenger() && !mob.isVehicle()
                && mob.distanceToSqr(player) > ALLY_MIN_DISTANCE * ALLY_MIN_DISTANCE;
    }

    private static boolean isBoss(Mob mob) {
        return mob instanceof TheIntoxicatorEntity || mob instanceof CorrosionQueenEntity || mob instanceof HumanHerderEntity || mob instanceof ResenterEntity;
    }

    private static boolean ready(Entity entity, String key, long now) {
        return now >= ModUtil.data(entity).getLong(key);
    }

    private static void setCooldown(AssimilatedEndermanEntity enderman, String key, long now, int min, int range) {
        ModUtil.data(enderman).putLong(key, now + min + enderman.getRandom().nextInt(range));
    }

    private static void warpEffects(ServerLevel level, Entity entity, double x, double y, double z, SoundEvent sound) {
        level.playSound(null, x, y, z, sound, SoundSource.HOSTILE, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.PORTAL, x, y + entity.getBbHeight() / 2.0, z, 48, entity.getBbWidth() / 2.0, entity.getBbHeight() / 2.0, entity.getBbWidth() / 2.0, 0.5);
    }

    private AssimilatedEndermanBehavior() {
    }
}
