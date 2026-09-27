package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.ParasiticShooterEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public final class ParasiticShooterSpots {
    public static void onTarget(ParasiticShooterEntity shooter) {
        if (!(shooter.level() instanceof ServerLevel level) || level.getRandom().nextDouble() > 0.03) {
            return;
        }
        Mob miter = ModEntities.MITER.get().create(level);
        if (miter == null) {
            return;
        }
        miter.moveTo(shooter.getX(), shooter.getY(), shooter.getZ(), level.getRandom().nextFloat() * 360.0F, 0.0F);
        ModUtil.finalizeSpawn(miter, level, level.getCurrentDifficultyAt(miter.blockPosition()), MobSpawnType.MOB_SUMMONED);
        level.addFreshEntity(miter);
    }

    private ParasiticShooterSpots() {
    }
}
