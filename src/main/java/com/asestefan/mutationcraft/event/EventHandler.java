package com.asestefan.mutationcraft.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface EventHandler {
    default void onLivingAttack(LivingEntity entity, DamageSource source) {
    }

    default float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        return amount;
    }

    default void onLivingDeath(LivingEntity entity, DamageSource source) {
    }

    default void onChangeTarget(LivingEntity entity, LivingEntity newTarget) {
    }

    default void onLivingTick(LivingEntity entity) {
    }

    default void onPlayerTick(Player player) {
    }

    default void onLevelTick(Level level) {
    }

    default boolean onEntityInteract(Player player, Entity target, InteractionHand hand) {
        return false;
    }

    default void onEffectExpired(LivingEntity entity, MobEffectInstance instance) {
    }

    default void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
    }
}
