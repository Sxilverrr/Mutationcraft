package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.HumanoidDeath;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class AssimilatedPillagerEntity extends MutantEntity {
    private static final int DEATH_TICKS = 55;

    public AssimilatedPillagerEntity(EntityType<? extends AssimilatedPillagerEntity> type, Level level) {
        super(type, level);
        this.xpReward = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.addStandardTargets(6);
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
        if (!this.level().isClientSide()) {
            this.playAnimation("death");
        }
        HumanoidDeath.execute(this, source, HumanoidDeath.SICKNESS);
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;
        if (this.deathTime >= DEATH_TICKS && !this.level().isClientSide() && !this.isRemoved()) {
            //? if >=1.21 {
            /*this.dropExperience(null);
            *///?} else {
            this.dropExperience();
            //?}
            this.remove(RemovalReason.KILLED);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 45.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_KNOCKBACK, 0.4);
    }
}
