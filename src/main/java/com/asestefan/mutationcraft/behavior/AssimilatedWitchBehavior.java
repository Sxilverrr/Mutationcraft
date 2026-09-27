package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.AssimilatedWitchEntity;
import java.util.Comparator;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public final class AssimilatedWitchBehavior {
    private static final double SPELL_AREA = 24.0;

    public static void hurt(AssimilatedWitchEntity witch, DamageSource source) {
        if (witch.level().isClientSide() || source.getEntity() == null || witch.getRandom().nextDouble() > 0.3) {
            return;
        }
        witch.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
        witch.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0));
    }

    public static void attack(LivingEntity target) {
        if (target.getRandom().nextDouble() <= 0.5) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
        }
    }

    public static void supportMutant(LivingEntity entity) {
        if (!ModUtil.isMutant(entity) || entity.getRandom().nextDouble() > 0.3) {
            return;
        }
        AABB area = AABB.ofSize(entity.position(), SPELL_AREA, SPELL_AREA, SPELL_AREA);
        AssimilatedWitchEntity witch = entity.level().getEntitiesOfClass(AssimilatedWitchEntity.class, area, e -> true)
                .stream()
                .min(Comparator.comparingDouble(e -> e.distanceToSqr(entity.getX(), entity.getY(), entity.getZ())))
                .orElse(null);
        if (witch == null) {
            return;
        }
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0));
        entity.level().playSound(null, entity.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
        witch.playAnimation("spell");
    }

    private AssimilatedWitchBehavior() {
    }
}
