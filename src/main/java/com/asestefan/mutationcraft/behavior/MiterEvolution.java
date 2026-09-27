package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.MiterEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public final class MiterEvolution {
    private static final String TIMER_KEY = "Timer";

    public static void tick(MiterEntity miter) {
        if (!(miter.level() instanceof ServerLevel level) || !miter.isAlive()) {
            return;
        }
        if (MutationcraftConfig.MITER_EVOLVES.get()) {
            CompoundTag data = ModUtil.data(miter);
            int timer = data.getInt(TIMER_KEY) + 1;
            data.putInt(TIMER_KEY, timer);
            if (timer >= MutationcraftConfig.MITER_EVOLVE_SECONDS.ticks()) {
                MutantConversion.evolve(miter, ModEntities.NECROPTOR.get(), ModSounds.MUTANT_TRANSFORM.get());
                return;
            }
        }
    }

    private MiterEvolution() {
    }
}
