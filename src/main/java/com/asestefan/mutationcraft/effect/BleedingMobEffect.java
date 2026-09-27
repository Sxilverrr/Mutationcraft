package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingMobEffect extends ModMobEffect {
    public BleedingMobEffect() {
        super(MobEffectCategory.HARMFUL, -8388608);
    }

    @Override
    protected void onTick(LivingEntity entity, int amplifier) {
        EffectProcedures.bleed(entity, amplifier);
    }
}
