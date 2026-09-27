package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SignOfFireMobEffect extends ModMobEffect {
    public SignOfFireMobEffect() {
        super(MobEffectCategory.HARMFUL, -6737152);
    }

    @Override
    protected void onStart(LivingEntity entity, int amplifier) {
        EffectProcedures.signOfFire(entity);
    }
}
