package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.behavior.VillageSirens;
import com.asestefan.mutationcraft.entity.HazmatFlamethrowerEntity;
import com.asestefan.mutationcraft.entity.HazmatGuardEntity;
import com.asestefan.mutationcraft.entity.HazmatLeaderEntity;
import com.asestefan.mutationcraft.entity.HazmatMedicEntity;
import com.asestefan.mutationcraft.entity.ScientistEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class HazmatEvents implements EventHandler {
    private static final double MEDIC_AREA = 32.0;

    public HazmatEvents() {
    }

    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        Entity attacker = source.getEntity();
        if (attacker instanceof HazmatGuardEntity) {
            level.sendParticles(ParticleTypes.SWEEP_ATTACK, entity.getX(), entity.getY(), entity.getZ(), 1, 1.0, 3.0, 1.0, 1.0);
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
        } else if (attacker instanceof HazmatLeaderEntity) {
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            level.sendParticles(ParticleTypes.SWEEP_ATTACK, entity.getX(), entity.getY(), entity.getZ(), 1, 1.0, 3.0, 1.0, 1.0);
        }
        if (isMedicPatient(entity) && !level.getEntitiesOfClass(HazmatMedicEntity.class, AABB.ofSize(entity.position(), MEDIC_AREA, MEDIC_AREA, MEDIC_AREA), medic -> true).isEmpty()) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
        }
    }

    @Override
    public void onLivingTick(LivingEntity entity) {
        VillageSirens.tick(entity);
    }

    private static boolean isMedicPatient(LivingEntity entity) {
        return entity instanceof ScientistEntity || entity instanceof HazmatMedicEntity || entity instanceof HazmatFlamethrowerEntity
                || entity instanceof HazmatGuardEntity || entity instanceof HazmatLeaderEntity;
    }
}
