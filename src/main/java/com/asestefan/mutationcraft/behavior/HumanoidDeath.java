package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public final class HumanoidDeath {
    public static final int SICKNESS = 2000;

    public static void execute(LivingEntity mob, DamageSource source, int sicknessTicks) {
        if (!(mob.level() instanceof ServerLevel level) || source.getEntity() == null) {
            return;
        }
        if (source.getEntity() instanceof LivingEntity killer) {
            killer.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), sicknessTicks, 0));
        }
        spawnParasites(level, mob.getX(), mob.getY(), mob.getZ());
    }

    private static void spawnParasites(ServerLevel level, double x, double y, double z) {
        RandomSource random = level.getRandom();
        double first = random.nextDouble();
        double second = random.nextDouble();
        if (first <= 0.05) {
            if (second <= 0.05) {
                for (int i = 0; i < 4; i++) {
                    spawn(level, ModEntities.MITER.get(), x, y, z);
                }
            }
        } else if (first <= 0.2 && second <= 0.2) {
            spawn(level, ModEntities.NECROPTOR.get(), x, y, z);
        }
    }

    private static void spawn(ServerLevel level, EntityType<? extends Mob> type, double x, double y, double z) {
        Mob mob = type.create(level);
        if (mob == null) {
            return;
        }
        mob.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(mob);
    }

    private HumanoidDeath() {
    }
}
