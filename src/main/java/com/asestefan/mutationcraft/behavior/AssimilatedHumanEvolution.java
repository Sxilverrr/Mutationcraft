package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.AssimilatedHumanEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;

public final class AssimilatedHumanEvolution {
    private static final String KEY = "mutationcraft:skeleton_kills";

    public static void onDeath(LivingEntity victim, DamageSource source) {
        if (!(victim instanceof Skeleton) || !(source.getEntity() instanceof AssimilatedHumanEntity human) || !human.isAlive()) {
            return;
        }
        CompoundTag data = ModUtil.data(human);
        int kills = data.getInt(KEY) + 1;
        if (kills < MutationcraftConfig.HUMAN_EVOLVE_KILLS.getInt()) {
            data.putInt(KEY, kills);
            return;
        }
        data.remove(KEY);
        MutantConversion.evolve(human, ModEntities.FLAYER.get(), ModSounds.MUTANT_TRANSFORM.get());
    }

    private AssimilatedHumanEvolution() {
    }
}
