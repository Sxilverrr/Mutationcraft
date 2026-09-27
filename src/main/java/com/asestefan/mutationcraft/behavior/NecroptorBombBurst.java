package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public final class NecroptorBombBurst {
    private static final double SPREAD = 1.5;

    public static void burst(ServerLevel level, Vec3 center) {
        RandomSource random = level.getRandom();
        int count = 2;
        if (random.nextDouble() < 0.25) {
            count++;
        }
        if (random.nextDouble() < 0.75) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            double x = center.x;
            double z = center.z;
            if (i > 0) {
                x += (random.nextDouble() * 2.0 - 1.0) * SPREAD;
                z += (random.nextDouble() * 2.0 - 1.0) * SPREAD;
            }
            AberrationSpawns.spawnNear(level, ModEntities.NECROPTOR.get(), x, center.y, z);
        }
        MutantConversion.effects(level, center.x, center.y, center.z, ModSounds.MUTANT_TRANSFORM.get());
    }

    private NecroptorBombBurst() {
    }
}
