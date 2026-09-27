package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.HazmatFlamethrowerEntity;
import com.asestefan.mutationcraft.entity.HazmatGuardEntity;
import com.asestefan.mutationcraft.entity.HazmatHelicopterEntity;
import com.asestefan.mutationcraft.entity.HazmatLeaderEntity;
import com.asestefan.mutationcraft.entity.ScientistEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import java.util.Comparator;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.AABB;

public final class HazmatHelicopterDrops {
    private static final int SLOW_FALLING_INTERVAL = 5;
    private static final double SLOW_FALLING_AREA = 24.0;
    private static final List<Class<? extends LivingEntity>> SLOW_FALLING_ORDER = List.of(HazmatGuardEntity.class, ScientistEntity.class,
            HazmatFlamethrowerEntity.class, HazmatLeaderEntity.class);

    public static void tick(HazmatHelicopterEntity helicopter) {
        if (!(helicopter.level() instanceof ServerLevel level)) {
            return;
        }
        boolean dropped = helicopter.canDrop() && drop(level, helicopter);
        if (dropped || helicopter.tickCount % SLOW_FALLING_INTERVAL == 0) {
            slowFalling(level, helicopter);
        }
    }

    private static boolean drop(ServerLevel level, HazmatHelicopterEntity helicopter) {
        RandomSource random = level.getRandom();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        EntityType<? extends Mob> type = null;
        if (r1 <= 0.05) {
            if (r2 <= 0.05) {
                type = ModEntities.HAZMAT_GUARD.get();
            } else if (r2 <= 0.06) {
                type = ModEntities.HAZMAT_LEADER.get();
            }
        } else if (r1 <= 0.07) {
            if (r2 <= 0.07) {
                type = ModEntities.HAZMAT_FLAMETHROWER.get();
            } else if (r2 <= 0.08) {
                type = ModEntities.HAZMAT_MEDIC.get();
            }
        }
        if (type == null) {
            return false;
        }
        Mob mob = type.create(level);
        if (mob == null) {
            return false;
        }
        mob.moveTo(helicopter.getX(), helicopter.getY(), helicopter.getZ(), random.nextFloat() * 360.0F, 0.0F);
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(mob);
        helicopter.countDrop();
        return true;
    }

    private static void slowFalling(ServerLevel level, HazmatHelicopterEntity helicopter) {
        AABB area = AABB.ofSize(helicopter.position(), SLOW_FALLING_AREA, SLOW_FALLING_AREA, SLOW_FALLING_AREA);
        for (Class<? extends LivingEntity> type : SLOW_FALLING_ORDER) {
            LivingEntity nearest = level.getEntitiesOfClass(type, area, entity -> true).stream()
                    .min(Comparator.comparingDouble((LivingEntity entity) -> entity.distanceToSqr(helicopter)))
                    .orElse(null);
            if (nearest != null) {
                nearest.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 0));
                return;
            }
        }
    }

    private HazmatHelicopterDrops() {
    }
}
