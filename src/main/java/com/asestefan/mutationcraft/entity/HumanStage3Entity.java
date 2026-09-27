package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.HumanoidDeath;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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

public class HumanStage3Entity extends MutantEntity {
    private static final int DEATH_TICKS = 12;

    public HumanStage3Entity(EntityType<? extends HumanStage3Entity> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.addStandardTargets(6);
    }

    @Override
    protected boolean weakConversion() {
        return true;
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        if (this.isSprinting() || this.isAggressive() && moving(event)) {
            return loop("run2");
        }
        return moving(event) ? loop("walk2") : loop("idle2");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.HUMAN_STAGE_AMBIENT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!this.level().isClientSide()) {
            this.playAnimation("death2");
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
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 85.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 11.0)
                .add(Attributes.FOLLOW_RANGE, 26.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.4);
    }
}
