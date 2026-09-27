package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public final class HazmatFlamethrowerDeath {
    public static void explode(ServerLevel level, double x, double y, double z) {
        if (level.getRandom().nextDouble() < 0.5) {
            level.explode(null, x, y, z, 2.0F, Level.ExplosionInteraction.MOB);
            level.playSound(null, BlockPos.containing(x, y, z), ModSounds.HAZMAT_FLAMETHROWER_EXPLODE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    private HazmatFlamethrowerDeath() {
    }
}
