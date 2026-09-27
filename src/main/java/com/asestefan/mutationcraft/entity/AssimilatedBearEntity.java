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

public class AssimilatedBearEntity extends MutantEntity {
    public AssimilatedBearEntity(EntityType<? extends AssimilatedBearEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.addLeapGoal(0.5F);
        this.addStandardTargets(7);
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        if (moving(event) && !this.isAggressive()) {
            return loop("walk2");
        }
        if (this.isSprinting() || this.isAggressive() && event.isMoving()) {
            return loop("run");
        }
        return loop("idle2");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ASSIMILATED_BEAR_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.ASSIMILATED_BEAR_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount + AnimalHurt.fireAspectBonus(source, false));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        AnimalDeath.execute(this, source, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 45.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.2)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6);
    }
}
