package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.TheIntoxicatorEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;

public final class RottenSkeletonAwaken {
    public static void execute(LivingEntity skeleton) {
        if (!(skeleton.level() instanceof ServerLevel level) || !skeleton.isAlive()) {
            return;
        }
        TheIntoxicatorEntity boss = ModEntities.THE_INTOXICATOR.get().create(level);
        if (boss != null) {
            boss.moveTo(skeleton.getX(), skeleton.getY(), skeleton.getZ(), level.getRandom().nextFloat() * 360.0F, 0.0F);
            ModUtil.finalizeSpawn(boss, level, level.getCurrentDifficultyAt(boss.blockPosition()), MobSpawnType.MOB_SUMMONED);
            level.addFreshEntity(boss);
        }
        skeleton.kill();
    }

    private RottenSkeletonAwaken() {
    }
}
