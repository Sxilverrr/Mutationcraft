package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public final class HumanStageEvolution {
    public static final String STAGE_1_KEY = "Evolve";
    public static final String STAGE_2_KEY = "Evolution";

    public static void tick(Mob mob, String key, EntityType<? extends Mob> next) {
        if (mob.level().isClientSide() || !mob.isAlive() || !MutationcraftConfig.HUMAN_STAGES_EVOLVE.get()) {
            return;
        }
        CompoundTag data = ModUtil.data(mob);
        int ticks = data.getInt(key) + 1;
        if (ticks < MutationcraftConfig.HUMAN_STAGE_EVOLVE_SECONDS.ticks()) {
            data.putInt(key, ticks);
            return;
        }
        data.remove(key);
        MutantConversion.evolve(mob, next, ModSounds.MUTANT_TRANSFORM.get());
    }

    private HumanStageEvolution() {
    }
}
