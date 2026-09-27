package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

public class AssimilatedVexEntity extends MutantEntity {
    private static final String LIFE_END = "mutationcraft:vex_life_end";
    private boolean swinging;
    private long lastSwing;

    public AssimilatedVexEntity(EntityType<? extends AssimilatedVexEntity> type, Level level) {
        super(type, level);
        this.xpReward = 2;
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    @Override
    protected String defaultTexture() {
        return "assimilated_vex";
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return this.onGround() ? loop("idle") : loop("charge");
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ChargeGoal());
        this.goalSelector.addGoal(3, this.meleeGoal(1.2));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.8, 20) {
            @Override
            protected Vec3 getPosition() {
                RandomSource random = AssimilatedVexEntity.this.getRandom();
                double x = AssimilatedVexEntity.this.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
                double y = AssimilatedVexEntity.this.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
                double z = AssimilatedVexEntity.this.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
                return new Vec3(x, y, z);
            }
        });
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        Class<?>[] targets = {Player.class, Villager.class, Pillager.class, IronGolem.class, Witch.class, Zombie.class, EnderMan.class, Evoker.class,
                HazmatLeaderEntity.class, HazmatGuardEntity.class, HazmatFlamethrowerEntity.class, HazmatHelicopterEntity.class, HazmatMedicEntity.class,
                ScientistEntity.class};
        int priority = 6;
        for (Class<?> target : targets) {
            this.targetSelector.addGoal(priority++, target(this, target));
        }
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    public void setLimitedLife(int ticks) {
        ModUtil.data(this).putLong(LIFE_END, this.level().getGameTime() + ticks);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide() && this.isAlive()) {
            CompoundTag data = ModUtil.data(this);
            long now = this.level().getGameTime();
            if (data.contains(LIFE_END) && now >= data.getLong(LIFE_END)) {
                data.putLong(LIFE_END, now + 20);
                this.hurt(this.damageSources().starve(), 1.0F);
            }
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.VEX_HURT;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.FALL) && !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setNoGravity(true);
    }

    private PlayState attackingPredicate(AnimationState<AssimilatedVexEntity> event) {
        if (this.getAttackAnim(event.getPartialTick()) > 0.0F && !this.swinging) {
            this.swinging = true;
            this.lastSwing = this.level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 15L <= this.level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay("attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "attacking", 4, this::attackingPredicate));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.9)
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3)
                .add(Attributes.FLYING_SPEED, 0.9);
    }

    private class ChargeGoal extends Goal {
        ChargeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return AssimilatedVexEntity.this.getTarget() != null && !AssimilatedVexEntity.this.getMoveControl().hasWanted();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = AssimilatedVexEntity.this.getTarget();
            return AssimilatedVexEntity.this.getMoveControl().hasWanted() && target != null && target.isAlive();
        }

        @Override
        public void start() {
            Vec3 eyes = AssimilatedVexEntity.this.getTarget().getEyePosition(1.0F);
            AssimilatedVexEntity.this.moveControl.setWantedPosition(eyes.x, eyes.y, eyes.z, 1.0);
        }

        @Override
        public void tick() {
            LivingEntity target = AssimilatedVexEntity.this.getTarget();
            if (target == null) {
                return;
            }
            if (AssimilatedVexEntity.this.getBoundingBox().intersects(target.getBoundingBox())) {
                AssimilatedVexEntity.this.doHurtTarget(target);
            } else if (AssimilatedVexEntity.this.distanceToSqr(target) < 16.0) {
                Vec3 eyes = target.getEyePosition(1.0F);
                AssimilatedVexEntity.this.moveControl.setWantedPosition(eyes.x, eyes.y, eyes.z, 1.0);
            }
        }
    }
}
