package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.CorrosionQueenAttack;
import com.asestefan.mutationcraft.behavior.CorrosionQueenSpells;
import com.asestefan.mutationcraft.behavior.ParasiticRamCharge;
import com.asestefan.mutationcraft.behavior.ParasiticShooterSpots;
import com.asestefan.mutationcraft.behavior.TheIntoxicatorBehavior;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.asestefan.mutationcraft.entity.HumanHerderEntity;
import com.asestefan.mutationcraft.entity.ParasiticRamEntity;
import com.asestefan.mutationcraft.entity.ParasiticRollerEntity;
import com.asestefan.mutationcraft.entity.ParasiticShooterEntity;
import com.asestefan.mutationcraft.entity.ResenterEntity;
import com.asestefan.mutationcraft.entity.TheIntoxicatorEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;

public class BossEvents implements EventHandler {
    public BossEvents() {
    }

    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker == null) {
            return;
        }
        if (attacker instanceof CorrosionQueenEntity && entity instanceof Player player) {
            CorrosionQueenAttack.corrode(player);
        }
        if (entity instanceof CorrosionQueenEntity queen && attacker instanceof Player player) {
            CorrosionQueenSpells.retaliate(queen, player);
        }
        if (attacker instanceof TheIntoxicatorEntity) {
            TheIntoxicatorBehavior.onAttack(entity);
        }
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (entity.level().isClientSide() || !(source.getEntity() instanceof LivingEntity attacker)) {
            return amount;
        }
        boolean playerOnly = entity instanceof CorrosionQueenEntity || entity instanceof HumanHerderEntity || entity instanceof ResenterEntity
                || entity instanceof ParasiticRamEntity || entity instanceof ParasiticRollerEntity || entity instanceof ParasiticShooterEntity;
        if (!(entity instanceof TheIntoxicatorEntity) && !(playerOnly && attacker instanceof Player)) {
            return amount;
        }
        int level = ModUtil.enchantmentLevel(Enchantments.FIRE_ASPECT, attacker.getMainHandItem());
        if (level == 1) {
            return amount + 2.0F;
        }
        if (level == 2) {
            return amount + 4.0F;
        }
        return amount;
    }

    @Override
    public void onLivingDeath(LivingEntity entity, DamageSource source) {
        Entity killer = source.getEntity();
        if (entity instanceof Player && killer instanceof CorrosionQueenEntity queen) {
            CorrosionQueenAttack.onKillPlayer(queen);
        }
        if (killer == null || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (entity instanceof CorrosionQueenEntity || entity instanceof HumanHerderEntity || entity instanceof ResenterEntity
                || entity instanceof ParasiticRamEntity || entity instanceof ParasiticRollerEntity) {
            brood(level, entity);
            sicken(killer);
        } else if (entity instanceof ParasiticShooterEntity) {
            sicken(killer);
        } else if (entity instanceof TheIntoxicatorEntity boss) {
            TheIntoxicatorBehavior.onDeath(boss, killer);
        }
    }

    @Override
    public void onChangeTarget(LivingEntity entity, LivingEntity newTarget) {
        if (entity instanceof ParasiticRamEntity ram) {
            ParasiticRamCharge.onTarget(ram);
        } else if (entity instanceof ParasiticShooterEntity shooter) {
            ParasiticShooterSpots.onTarget(shooter);
        }
    }

    private static void brood(ServerLevel level, LivingEntity entity) {
        double r1 = MutationcraftConfig.MUTANTS_SPAWN_PARASITES.get() ? level.getRandom().nextDouble() : 1.0;
        double r2 = level.getRandom().nextDouble();
        if (r1 <= 0.05) {
            if (r2 <= 0.05) {
                for (int i = 0; i < 4; i++) {
                    summon(level, ModEntities.MITER.get(), entity);
                }
            }
        } else if (r1 <= 0.2 && r2 <= 0.2) {
            summon(level, ModEntities.NECROPTOR.get(), entity);
        }
    }

    private static void summon(ServerLevel level, EntityType<? extends Mob> type, LivingEntity at) {
        Mob mob = type.create(level);
        if (mob == null) {
            return;
        }
        mob.moveTo(at.getX(), at.getY(), at.getZ(), level.getRandom().nextFloat() * 360.0F, 0.0F);
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(mob);
    }

    private static void sicken(Entity killer) {
        if (killer instanceof LivingEntity living && MutationcraftConfig.KILLING_MUTANTS_GIVES_SICKNESS.get()) {
            living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 2000, 0));
        }
    }
}
