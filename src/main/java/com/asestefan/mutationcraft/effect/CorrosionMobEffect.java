package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CorrosionMobEffect extends ModMobEffect {
    public CorrosionMobEffect() {
        super(MobEffectCategory.HARMFUL, -14286591);
    }

    @Override
    protected void onStart(LivingEntity entity, int amplifier) {
        EffectProcedures.corrodeArmor(entity, amplifier);
    }
}
