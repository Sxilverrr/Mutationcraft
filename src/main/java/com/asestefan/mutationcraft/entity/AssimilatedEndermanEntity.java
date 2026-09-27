package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.AssimilatedEndermanBehavior;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class AssimilatedEndermanEntity extends MutantEntity {
    public AssimilatedEndermanEntity(EntityType<? extends AssimilatedEndermanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected String defaultTexture() {
        return "assimilated_enderman";
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        if (moving(event)) {
            event.getController().setAnimationSpeed(this.isAggressive() ? 1.6 : 1.0);
            return loop("walk");
        }
        event.getController().setAnimationSpeed(1.0);
        return loop("idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.addStandardTargets(6);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ASSIMILATED_ENDERMAN_AMBIENT.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ASSIMILATED_ENDERMAN_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.ASSIMILATED_ENDERMAN_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        AssimilatedEndermanBehavior.hurt(this, source);
        return !source.is(DamageTypes.FALL) && !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide()) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5),
                        (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
            }
        }
        AssimilatedEndermanBehavior.tick(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.45)
                .add(Attributes.MAX_HEALTH, 55.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.4);
    }
}
