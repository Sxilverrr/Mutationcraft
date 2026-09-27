package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SeverenMobEffect extends ModMobEffect {
    public SeverenMobEffect() {
        super(MobEffectCategory.HARMFUL, -16764160);
    }

    @Override
    protected void onTick(LivingEntity entity, int amplifier) {
        EffectProcedures.severen(entity, amplifier);
    }
}
