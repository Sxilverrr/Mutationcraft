package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class AssimilatedRoamerAttack {
    public static void assimilated(LivingEntity victim, LivingEntity roamer) {
        if (roamer.getRandom().nextDouble() < 0.2) {
            victim.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0));
        }
        speedUp(roamer, 3);
    }

    public static void developed(LivingEntity victim, LivingEntity roamer) {
        if (roamer.getRandom().nextDouble() <= 0.2) {
            victim.addEffect(new MobEffectInstance(ModMobEffects.SEVEREN.ref(), 160, 0));
        }
        speedUp(roamer, 4);
    }

    public static void developedHalfHealth(LivingEntity roamer) {
        if (roamer.getHealth() <= 25.0F) {
            roamer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 1));
        }
    }

    private static void speedUp(LivingEntity roamer, int amplifier) {
        MutationcraftMod.queueServerWork(roamer.level(), 40, () -> {
            if (roamer.isAlive()) {
                roamer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, amplifier));
            }
        });
    }

    private AssimilatedRoamerAttack() {
    }
}
