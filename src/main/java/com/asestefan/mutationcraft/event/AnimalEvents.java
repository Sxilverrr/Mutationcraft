package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.behavior.AssimilatedBearAttack;
import com.asestefan.mutationcraft.behavior.AssimilatedFoxTarget;
import com.asestefan.mutationcraft.behavior.AssimilatedJockeyAttack;
import com.asestefan.mutationcraft.entity.AssimilatedBearEntity;
import com.asestefan.mutationcraft.entity.AssimilatedFoxEntity;
import com.asestefan.mutationcraft.entity.AssimilatedJockeyEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class AnimalEvents implements EventHandler {
    public AnimalEvents() {
    }

    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        if (source.getEntity() instanceof AssimilatedBearEntity) {
            AssimilatedBearAttack.onHit(entity);
        } else if (source.getEntity() instanceof AssimilatedJockeyEntity jockey) {
            AssimilatedJockeyAttack.onHit(jockey, entity);
        }
    }

    @Override
    public void onChangeTarget(LivingEntity entity, LivingEntity newTarget) {
        if (entity instanceof AssimilatedFoxEntity fox) {
            AssimilatedFoxTarget.onTarget(fox);
        }
    }
}
