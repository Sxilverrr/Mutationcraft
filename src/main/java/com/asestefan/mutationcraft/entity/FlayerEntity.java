package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.FlayerBehavior;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class FlayerEntity extends MutantEntity {
    public static final EntityDataAccessor<Boolean> BURROWED = SynchedEntityData.defineId(FlayerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final RawAnimation BURROWED_POSE = RawAnimation.begin().thenPlayAndHold("digdown");
    private static final int SINK_TICKS = 26;
    private static final int BURROW_TICKS = 100;
    private static final int RESURFACE_TICKS = 28;
    private static final double RESURFACE_RANGE = 32.0;
    private static final double RESURFACE_DISTANCE = 3.0;
    private int burrowTicks;
    private int burrowCooldown;

    public FlayerEntity(EntityType<? extends FlayerEntity> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    @Override
    protected String defaultTexture() {
        return "flayer";
    }

    //? if >=1.21 {
    /*@Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BURROWED, false);
    }
    *///?} else {
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BURROWED, false);
    }
    //?}

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return this.isBurrowed() ? BURROWED_POSE : loop("idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.targetSelector.addGoal(6, target(this, Player.class));
        this.targetSelector.addGoal(7, target(this, HazmatLeaderEntity.class));
        this.targetSelector.addGoal(8, target(this, HazmatGuardEntity.class));
        this.targetSelector.addGoal(9, target(this, HazmatFlamethrowerEntity.class));
        this.targetSelector.addGoal(10, target(this, HazmatHelicopterEntity.class));
        this.targetSelector.addGoal(11, target(this, IronGolem.class));
        this.targetSelector.addGoal(12, target(this, Villager.class));
        this.targetSelector.addGoal(13, target(this, HazmatMedicEntity.class));
        this.targetSelector.addGoal(14, target(this, ScientistEntity.class));
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    public boolean isBurrowed() {
        return this.entityData.get(BURROWED);
    }

    public boolean isHidden() {
        return this.isBurrowed() && this.burrowTicks >= SINK_TICKS;
    }

    public void tryBurrow() {
        if (this.level().isClientSide() || this.isBurrowed() || this.burrowCooldown > 0 || !this.isAlive()) {
            return;
        }
        this.burrowTicks = 0;
        this.entityData.set(BURROWED, true);
    }

    private void tickBurrow() {
        if (this.burrowCooldown > 0) {
            this.burrowCooldown--;
        }
        if (!this.isBurrowed()) {
            return;
        }
        this.burrowTicks++;
        if (this.burrowTicks >= BURROW_TICKS) {
            this.resurface();
        }
    }

    private void resurface() {
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive() && this.distanceToSqr(target) <= RESURFACE_RANGE * RESURFACE_RANGE) {
            double angle = this.getRandom().nextDouble() * Math.PI * 2.0;
            ModUtil.teleportNear(this, target.getX() + Math.cos(angle) * RESURFACE_DISTANCE, target.getY(), target.getZ() + Math.sin(angle) * RESURFACE_DISTANCE);
        }
        this.entityData.set(BURROWED, false);
        this.burrowTicks = 0;
        this.burrowCooldown = RESURFACE_TICKS;
        this.playAnimation("digup");
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.DROWN)) {
            return false;
        }
        if (this.isHidden() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return !this.isBurrowed() && super.doHurtTarget(target);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        FlayerBehavior.onDeath(this, source.getEntity());
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide()) {
            this.tickBurrow();
        }
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
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 4.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2);
    }
}
