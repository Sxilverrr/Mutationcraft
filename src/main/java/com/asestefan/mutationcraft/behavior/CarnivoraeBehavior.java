package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.CarnivoraeEntity;
import com.asestefan.mutationcraft.entity.MiterEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class CarnivoraeBehavior {
    private static final double MITER_RADIUS = 16.0;

    public static void tick(CarnivoraeEntity carnivorae) {
        if (!(carnivorae.level() instanceof ServerLevel level) || !carnivorae.isAlive()) {
            return;
        }
        double r1 = level.getRandom().nextDouble();
        double r2 = level.getRandom().nextDouble();
        if (r1 <= 0.04) {
            if (r2 <= 0.04) {
                summonMiter(level, carnivorae, 1.0);
            }
        } else if (r1 <= 0.05 && r2 <= 0.05) {
            summonMiter(level, carnivorae, -1.0);
        }
    }

    private static void summonMiter(ServerLevel level, CarnivoraeEntity carnivorae, double offsetZ) {
        if (level.getEntitiesOfClass(MiterEntity.class, carnivorae.getBoundingBox().inflate(MITER_RADIUS)).size() >= MutationcraftConfig.CARNIVORAE_MITER_LIMIT.getInt()) {
            return;
        }
        AberrationSpawns.spawnNear(level, ModEntities.MITER.get(), carnivorae.getX(), carnivorae.getY(), carnivorae.getZ() + offsetZ);
    }

    public static void onDeath(CarnivoraeEntity carnivorae, Entity killer) {
        if (killer == null) {
            return;
        }
        AberrationSpawns.deathSpawns(carnivorae);
        AberrationSpawns.mutagenSickness(killer, 2000);
    }

    public static void onAttack(LivingEntity victim, Entity attacker) {
        if (!(attacker instanceof CarnivoraeEntity) || !(victim.level() instanceof ServerLevel level)) {
            return;
        }
        victim.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING.ref(), 120, 0));
        victim.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        double x = victim.getX();
        double y = victim.getY();
        double z = victim.getZ();
        level.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 3, 1.1, 1.6, 1.1, 1.0);
        level.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 4, 0.6, 1.7, 0.6, 1.0);
        level.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 3, 0.7, 1.5, 0.7, 1.0);
        level.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 2, 0.4, 1.2, 0.6, 1.0);
        level.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 5, 0.9, 1.4, 0.9, 1.0);
    }

    private CarnivoraeBehavior() {
    }
}
