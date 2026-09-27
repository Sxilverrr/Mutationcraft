package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.CarnivoraeBehavior;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
//?}

public class CarnivoraeEntity extends MutantEntity {
    private static final RawAnimation EAT = RawAnimation.begin().thenPlay("eat");
    private boolean swinging;
    private long lastSwing;

    public CarnivoraeEntity(EntityType<? extends CarnivoraeEntity> type, Level level) {
        super(type, level);
        this.xpReward = 9;
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return loop("idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
            //? if >=1.21 {
            /*@Override
            protected boolean canPerformAttack(LivingEntity entity) {
                return this.isTimeToAttack() && this.mob.distanceToSqr(entity) <= carnivoraeReach(this.mob, entity) && this.mob.getSensing().hasLineOfSight(entity);
            }
            *///?} else {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return carnivoraeReach(this.mob, entity);
            }
            //?}
        });
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, target(this, Player.class));
        this.targetSelector.addGoal(5, target(this, IronGolem.class));
        this.targetSelector.addGoal(6, target(this, HazmatLeaderEntity.class));
        this.targetSelector.addGoal(7, target(this, HazmatGuardEntity.class));
        this.targetSelector.addGoal(8, target(this, HazmatFlamethrowerEntity.class));
        this.targetSelector.addGoal(9, target(this, HazmatMedicEntity.class));
        this.targetSelector.addGoal(10, target(this, Villager.class));
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    private static double carnivoraeReach(Mob mob, LivingEntity target) {
        double width = mob.getBbWidth() * 2.0;
        return Math.max(6.25, width * width + target.getBbWidth());
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.CARNIVORAE_AMBIENT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        CarnivoraeBehavior.onDeath(this, source.getEntity());
    }

    @Override
    public void baseTick() {
        super.baseTick();
        CarnivoraeBehavior.tick(this);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 70.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.1);
    }

    private PlayState attackingPredicate(AnimationState<CarnivoraeEntity> event) {
        if (this.getAttackAnim(event.getPartialTick()) > 0.0F && !this.swinging) {
            this.swinging = true;
            this.lastSwing = this.level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 15L <= this.level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(EAT);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "attacking", 4, this::attackingPredicate));
    }
}
