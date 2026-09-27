package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.entity.ReductorEntity;
import net.minecraft.world.entity.Entity;

public final class ReductorBehavior {
    public static void onDeath(ReductorEntity reductor, Entity killer) {
        if (killer == null) {
            return;
        }
        AberrationSpawns.deathSpawns(reductor);
        AberrationSpawns.mutagenSickness(killer, 2000);
    }

    private ReductorBehavior() {
    }
}
