package com.asestefan.mutationcraft.behavior;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class AssimilatedWanderingTraderHurt {
    public static void execute(LivingEntity trader) {
        if (trader.level().isClientSide() || !trader.isAlive()) {
            return;
        }
        RandomSource random = trader.getRandom();
        double first = random.nextDouble();
        double second = random.nextDouble();
        if (first <= 0.5) {
            if (second <= 0.3) {
                drink(trader, new MobEffectInstance(MobEffects.INVISIBILITY, 300, 0));
            }
        } else if (first <= 0.6 && second <= 0.5) {
            drink(trader, new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
        }
    }

    private static void drink(LivingEntity trader, MobEffectInstance effect) {
        trader.level().playSound(null, trader.getX(), trader.getY(), trader.getZ(), SoundEvents.WANDERING_TRADER_DRINK_POTION, SoundSource.NEUTRAL, 1.0F, 1.0F);
        trader.addEffect(effect);
    }

    private AssimilatedWanderingTraderHurt() {
    }
}
