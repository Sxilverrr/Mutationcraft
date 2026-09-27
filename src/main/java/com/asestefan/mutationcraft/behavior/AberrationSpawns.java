package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public final class AberrationSpawns {
    public static Mob spawnAt(ServerLevel level, EntityType<? extends Mob> type, double x, double y, double z) {
        Mob mob = type.create(level);
        if (mob == null) {
            return null;
        }
        mob.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(mob);
        return mob;
    }

    public static Mob spawnNear(ServerLevel level, EntityType<? extends Mob> type, double x, double y, double z) {
        Mob mob = type.create(level);
        if (mob == null) {
            return null;
        }
        mob.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
        if (!ModUtil.teleportNear(mob, x, y, z)) {
            mob.discard();
            return null;
        }
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(mob);
        return mob;
    }

    public static void deathSpawns(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        RandomSource random = level.getRandom();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        if (r1 <= 0.05) {
            if (r2 <= 0.05) {
                for (int i = 0; i < 4; i++) {
                    spawnAt(level, ModEntities.MITER.get(), entity.getX(), entity.getY(), entity.getZ());
                }
            }
        } else if (r1 <= 0.2 && r2 <= 0.2) {
            spawnAt(level, ModEntities.NECROPTOR.get(), entity.getX(), entity.getY(), entity.getZ());
        }
    }

    public static void mutagenSickness(Entity killer, int duration) {
        if (killer instanceof LivingEntity living && !living.level().isClientSide()) {
            living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), duration, 0));
        }
    }

    private AberrationSpawns() {
    }
}
