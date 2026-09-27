package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.AssimilatedEndermanBehavior;
import com.asestefan.mutationcraft.behavior.AssimilatedEvokerBehavior;
import com.asestefan.mutationcraft.behavior.AssimilatedWitchBehavior;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.AssimilatedEndermanEntity;
import com.asestefan.mutationcraft.entity.AssimilatedEvokerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedVexEntity;
import com.asestefan.mutationcraft.entity.AssimilatedWitchEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;

public class IllagerEvents implements EventHandler {
    public IllagerEvents() {
    }

    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        Entity attacker = source.getEntity();
        AssimilatedWitchBehavior.supportMutant(entity);
        if (attacker == null) {
            return;
        }
        if (attacker instanceof AssimilatedWitchEntity) {
            AssimilatedWitchBehavior.attack(entity);
        }
        if (attacker instanceof AssimilatedEvokerEntity) {
            AssimilatedEvokerBehavior.attack(entity);
        }
        if (entity instanceof AssimilatedEvokerEntity evoker && attacker instanceof Player player) {
            AssimilatedEvokerBehavior.retaliate(evoker, player);
        }
        if (attacker instanceof AssimilatedEndermanEntity enderman) {
            AssimilatedEndermanBehavior.attack(enderman, entity);
        }
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!isOwnMob(entity) || !(source.getEntity() instanceof Player player)) {
            return amount;
        }
        int fireAspect = ModUtil.enchantmentLevel(Enchantments.FIRE_ASPECT, player.getMainHandItem());
        if (fireAspect == 1) {
            return amount + 2.0F;
        }
        if (fireAspect == 2) {
            return amount + 4.0F;
        }
        return amount;
    }

    @Override
    public void onLivingDeath(LivingEntity entity, DamageSource source) {
        Entity killer = source.getEntity();
        if (entity instanceof Player player && killer instanceof AssimilatedEndermanEntity enderman) {
            AssimilatedEndermanBehavior.onPlayerKilled(player, enderman);
        }
        if (killer != null && (entity instanceof AssimilatedWitchEntity || entity instanceof AssimilatedEvokerEntity || entity instanceof AssimilatedEndermanEntity)) {
            deathSpawns(entity, killer);
        }
    }

    @Override
    public void onChangeTarget(LivingEntity entity, LivingEntity newTarget) {
        if (entity instanceof AssimilatedVexEntity vex) {
            vex.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 3));
        }
    }

    private static boolean isOwnMob(LivingEntity entity) {
        return entity instanceof AssimilatedWitchEntity || entity instanceof AssimilatedEvokerEntity || entity instanceof AssimilatedVexEntity
                || entity instanceof AssimilatedEndermanEntity;
    }

    private static void deathSpawns(LivingEntity entity, Entity killer) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        double roll = MutationcraftConfig.MUTANTS_SPAWN_PARASITES.get() ? level.getRandom().nextDouble() : 1.0;
        double chance = level.getRandom().nextDouble();
        if (roll <= 0.05) {
            if (chance <= 0.05) {
                for (int i = 0; i < 4; i++) {
                    spawn(level, ModEntities.MITER.get(), entity);
                }
            }
        } else if (roll <= 0.2 && chance <= 0.2) {
            spawn(level, ModEntities.NECROPTOR.get(), entity);
        }
        if (killer instanceof LivingEntity living && MutationcraftConfig.KILLING_MUTANTS_GIVES_SICKNESS.get()) {
            living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 2000, 0));
        }
    }

    private static void spawn(ServerLevel level, EntityType<? extends Mob> type, LivingEntity at) {
        Mob mob = type.create(level);
        if (mob == null) {
            return;
        }
        mob.moveTo(at.getX(), at.getY(), at.getZ(), level.getRandom().nextFloat() * 360.0F, 0.0F);
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(mob);
    }
}
