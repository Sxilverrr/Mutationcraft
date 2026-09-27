package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.AssimilatedJockeyEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class AssimilatedJockeyAttack {
    public static void onHit(AssimilatedJockeyEntity jockey, LivingEntity target) {
        if (jockey.getRandom().nextDouble() > 0.2) {
            return;
        }
        jockey.playAnimation("attack");
        MutationcraftMod.queueServerWork(target.level(), 20, () -> {
            target.hurt(target.damageSources().generic(), 8.0F);
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
        });
    }

    private AssimilatedJockeyAttack() {
    }
}
