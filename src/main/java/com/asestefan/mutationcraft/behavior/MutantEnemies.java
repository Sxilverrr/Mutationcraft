package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;

public final class MutantEnemies {
    private static final int PRIORITY = 3;

    public static void onJoin(Entity entity) {
        if (!(entity instanceof Mob mob) || !(entity instanceof Enemy) || mob.level().isClientSide() || !MutationcraftConfig.HOSTILES_FIGHT_MUTANTS.get()
                || ModUtil.isMutant(mob) || MutationcraftMod.MODID.equals(BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).getNamespace())) {
            return;
        }
        GoalSelector targets = Services.PLATFORM.targetSelector(mob);
        for (var goal : targets.getAvailableGoals()) {
            if (goal.getGoal() instanceof HuntMutantsGoal) {
                return;
            }
        }
        targets.addGoal(PRIORITY, new HuntMutantsGoal(mob));
    }

    private static final class HuntMutantsGoal extends NearestAttackableTargetGoal<LivingEntity> {
        private HuntMutantsGoal(Mob mob) {
            super(mob, LivingEntity.class, 10, true, false, ModUtil::isMutant);
        }
    }

    private MutantEnemies() {
    }
}
