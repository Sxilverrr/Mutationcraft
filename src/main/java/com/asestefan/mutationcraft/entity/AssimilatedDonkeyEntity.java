package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.AnimalDeath;
import com.asestefan.mutationcraft.behavior.AnimalHurt;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class AssimilatedDonkeyEntity extends MutantEntity {
    public AssimilatedDonkeyEntity(EntityType<? extends AssimilatedDonkeyEntity> type, Level level) {
        super(type, level);
        this.xpReward = 7;
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
        if (moving(event) && !this.isAggressive() || this.isSprinting() || this.isAggressive() && event.isMoving()) {
            return loop("walk");
        }
        return loop("idle");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ASSIMILATED_DONKEY_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SKELETON_HORSE_GALLOP_WATER, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DONKEY_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount + AnimalHurt.fireAspectBonus(source, true));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        AnimalDeath.execute(this, source, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 35.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9)
                .add(Attributes.ATTACK_KNOCKBACK, 0.9);
    }
}
