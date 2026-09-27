package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.entity.AssimilatedBearEntity;
import com.asestefan.mutationcraft.entity.AssimilatedCowEntity;
import com.asestefan.mutationcraft.entity.AssimilatedCreeperEntity;
import com.asestefan.mutationcraft.entity.AssimilatedDonkeyEntity;
import com.asestefan.mutationcraft.entity.AssimilatedEndermanEntity;
import com.asestefan.mutationcraft.entity.AssimilatedEvokerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedFoxEntity;
import com.asestefan.mutationcraft.entity.AssimilatedHorseEntity;
import com.asestefan.mutationcraft.entity.AssimilatedHumanEntity;
import com.asestefan.mutationcraft.entity.AssimilatedJockeyEntity;
import com.asestefan.mutationcraft.entity.AssimilatedPigEntity;
import com.asestefan.mutationcraft.entity.AssimilatedPiglinEntity;
import com.asestefan.mutationcraft.entity.AssimilatedPillagerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedRoamerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedSheepEntity;
import com.asestefan.mutationcraft.entity.AssimilatedSpiderEntity;
import com.asestefan.mutationcraft.entity.AssimilatedVexEntity;
import com.asestefan.mutationcraft.entity.AssimilatedVillagerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedWanderingTraderEntity;
import com.asestefan.mutationcraft.entity.AssimilatedWitchEntity;
import com.asestefan.mutationcraft.entity.AssimilatedWolfEntity;
import com.asestefan.mutationcraft.entity.CarnivoraeEntity;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.asestefan.mutationcraft.entity.DevelopedRoamerEntity;
import com.asestefan.mutationcraft.entity.FlayerEntity;
import com.asestefan.mutationcraft.entity.HeavyHookEntity;
import com.asestefan.mutationcraft.entity.HumanHerderEntity;
import com.asestefan.mutationcraft.entity.HumanStage1Entity;
import com.asestefan.mutationcraft.entity.HumanStage2Entity;
import com.asestefan.mutationcraft.entity.HumanStage3Entity;
import com.asestefan.mutationcraft.entity.LightHookEntity;
import com.asestefan.mutationcraft.entity.MediumHookEntity;
import com.asestefan.mutationcraft.entity.MiterEntity;
import com.asestefan.mutationcraft.entity.NecroptorEntity;
import com.asestefan.mutationcraft.entity.ParasiticRamEntity;
import com.asestefan.mutationcraft.entity.ParasiticRollerEntity;
import com.asestefan.mutationcraft.entity.ParasiticShooterEntity;
import com.asestefan.mutationcraft.entity.ReductorEntity;
import com.asestefan.mutationcraft.entity.ResenterEntity;
import com.asestefan.mutationcraft.entity.TheIntoxicatorEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public final class HazmatTargets {
    private static final Class<?>[] MUTANTS = {AssimilatedHumanEntity.class, AssimilatedPigEntity.class, AssimilatedPillagerEntity.class,
            AssimilatedRoamerEntity.class, AssimilatedSheepEntity.class, AssimilatedWanderingTraderEntity.class, AssimilatedPiglinEntity.class,
            HumanStage3Entity.class, DevelopedRoamerEntity.class, NecroptorEntity.class, MiterEntity.class, HumanStage2Entity.class,
            AssimilatedHorseEntity.class, HumanStage1Entity.class, AssimilatedVillagerEntity.class, AssimilatedJockeyEntity.class,
            AssimilatedEndermanEntity.class, TheIntoxicatorEntity.class, AssimilatedCowEntity.class, AssimilatedWitchEntity.class, ReductorEntity.class,
            AssimilatedWolfEntity.class, AssimilatedFoxEntity.class, AssimilatedBearEntity.class, AssimilatedSpiderEntity.class,
            AssimilatedEvokerEntity.class, AssimilatedVexEntity.class, CarnivoraeEntity.class, AssimilatedDonkeyEntity.class, FlayerEntity.class,
            CorrosionQueenEntity.class, HumanHerderEntity.class, ResenterEntity.class, ParasiticRamEntity.class, ParasiticRollerEntity.class,
            ParasiticShooterEntity.class, LightHookEntity.class, MediumHookEntity.class, HeavyHookEntity.class, AssimilatedCreeperEntity.class};

    public static void add(Mob mob, GoalSelector selector, int priority) {
        for (Class<?> type : MUTANTS) {
            selector.addGoal(priority++, target(mob, type));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static NearestAttackableTargetGoal<?> target(Mob mob, Class<?> type) {
        return new NearestAttackableTargetGoal(mob, type, true, true);
    }

    private HazmatTargets() {
    }
}
