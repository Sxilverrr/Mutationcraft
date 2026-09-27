package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.entity.FlayerEntity;
import com.asestefan.mutationcraft.entity.NecroptorBombEntity;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class FlayerBehavior {
    public static void onDeath(FlayerEntity flayer, Entity killer) {
        if (killer == null) {
            return;
        }
        AberrationSpawns.mutagenSickness(killer, 2000);
        AberrationSpawns.deathSpawns(flayer);
    }

    public static void onHurtTarget(LivingEntity victim, Entity attacker) {
        if (attacker instanceof FlayerEntity flayer) {
            flayer.playAnimation("attack");
            if (!victim.level().isClientSide()) {
                victim.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING.ref(), 200, 0));
            }
        }
    }

    public static void onAttacked(LivingEntity entity, Entity attacker) {
        if (!(entity instanceof FlayerEntity flayer) || attacker == null) {
            return;
        }
        if (attacker instanceof Player && flayer.getRandom().nextDouble() <= 0.15) {
            NecroptorBombEntity.throwFrom(flayer, 1.0F, 0.2F, 1.0);
        }
        if (flayer.getRandom().nextDouble() <= 0.6) {
            flayer.tryBurrow();
        }
    }

    private FlayerBehavior() {
    }
}
