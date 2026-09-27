package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.MutationcraftMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
            assimilatedBearAttack(entity);
        } else if (source.getEntity() instanceof AssimilatedJockeyEntity jockey) {
            assimilatedJockeyAttack(jockey, entity);
        }
    }

    @Override
    public void onChangeTarget(LivingEntity entity, LivingEntity newTarget) {
        if (entity instanceof AssimilatedFoxEntity fox) {
            assimilatedFoxTarget(fox);
        }
    }

    private static void assimilatedBearAttack(LivingEntity target) {
        target.setTicksFrozen(60);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
    }

    private static void assimilatedFoxTarget(AssimilatedFoxEntity fox) {
        if (fox.getRandom().nextDouble() <= 0.3) {
            fox.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
        }
    }

    private static void assimilatedJockeyAttack(AssimilatedJockeyEntity jockey, LivingEntity target) {
        if (jockey.getRandom().nextDouble() > 0.2) {
            return;
        }
        jockey.playAnimation("attack");
        MutationcraftMod.queueServerWork(target.level(), 20, () -> {
            target.hurt(target.damageSources().generic(), 8.0F);
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
        });
    }
}
