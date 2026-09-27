package com.asestefan.mutationcraft.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public final class ScientistDeath {
    public static void breakSerum(ServerLevel level, double x, double y, double z) {
        if (level.getRandom().nextDouble() <= 0.2) {
            level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    private ScientistDeath() {
    }
}
