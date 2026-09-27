package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.AssimilatedWanderingTraderHurt;
import com.asestefan.mutationcraft.behavior.HumanoidDeath;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class AssimilatedWanderingTraderEntity extends MutantEntity {
    public AssimilatedWanderingTraderEntity(EntityType<? extends AssimilatedWanderingTraderEntity> type, Level level) {
        super(type, level);
        this.xpReward = 7;
        if (this.getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new PanicGoal(this, 1.2));
        this.addStandardTargets(8);
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
    public boolean hurt(DamageSource source, float amount) {
        AssimilatedWanderingTraderHurt.execute(this);
        return super.hurt(source, amount);
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
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5);
    }
}
