package com.asestefan.mutationcraft.behavior;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class AssimilatedBearAttack {
    public static void onHit(LivingEntity target) {
        target.setTicksFrozen(60);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
    }

    private AssimilatedBearAttack() {
    }
}
