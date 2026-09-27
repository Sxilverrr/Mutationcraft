package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.AnimalDeath;
import com.asestefan.mutationcraft.behavior.AnimalHurt;
import com.asestefan.mutationcraft.behavior.AnimalSwingAnimation;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class AssimilatedHorseEntity extends MutantEntity {
    private final AnimalSwingAnimation swing = new AnimalSwingAnimation(this, "bite");

    public AssimilatedHorseEntity(EntityType<? extends AssimilatedHorseEntity> type, Level level) {
        super(type, level);
        this.xpReward = 7;
    }

    @Override
    protected String defaultTexture() {
        return "assimilated_horse";
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
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        if (moving(event)) {
            return loop("walk");
        }
        if (this.isSprinting()) {
            return loop("run");
        }
        return loop("idle");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "attacking", 4, this.swing::predicate));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ASSIMILATED_HORSE_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SKELETON_HORSE_GALLOP_WATER, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.MUTANT_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_ANIMAL_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN)) {
            return false;
        }
        return super.hurt(source, amount + AnimalHurt.fireAspectBonus(source, false));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        AnimalDeath.execute(this, source, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9)
                .add(Attributes.ATTACK_KNOCKBACK, 0.9);
    }
}
