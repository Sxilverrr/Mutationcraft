package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.entity.AssimilatedFoxEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class AssimilatedFoxTarget {
    public static void onTarget(AssimilatedFoxEntity fox) {
        if (fox.getRandom().nextDouble() <= 0.3) {
            fox.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
        }
    }

    private AssimilatedFoxTarget() {
    }
}
