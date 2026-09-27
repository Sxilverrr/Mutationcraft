package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.HumanoidDeath;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class AssimilatedHumanEntity extends MutantEntity {
    public AssimilatedHumanEntity(EntityType<? extends AssimilatedHumanEntity> type, Level level) {
        super(type, level);
        this.xpReward = 7;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        Class<?>[] targets = {Player.class, HazmatLeaderEntity.class, HazmatGuardEntity.class, HazmatFlamethrowerEntity.class, HazmatHelicopterEntity.class,
                IronGolem.class, Skeleton.class, Villager.class, Pillager.class, HazmatMedicEntity.class, WanderingTrader.class, Zombie.class, Witch.class,
                EnderMan.class, ScientistEntity.class, Evoker.class};
        int priority = 6;
        for (Class<?> target : targets) {
            this.targetSelector.addGoal(priority++, target(this, target));
        }
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("walk") : loop("idle");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.MUTANT_HUMAN_AMBIENT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        HumanoidDeath.execute(this, source, HumanoidDeath.SICKNESS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }
}
