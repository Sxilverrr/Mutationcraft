package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.init.ModSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class ParasiticShooterEntity extends MutantEntity implements RangedAttackMob {
    public ParasiticShooterEntity(EntityType<? extends ParasiticShooterEntity> type, Level level) {
        super(type, level);
        this.xpReward = 12;
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    @Override
    protected String defaultTexture() {
        return "parasitic_shooter";
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return !this.onGround() || moving(event) ? loop("fly") : loop("idle");
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ShooterAttackGoal(this, 1.25, 100, 100, 120.0F));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.7, 20) {
            @Override
            protected Vec3 getPosition() {
                RandomSource random = ParasiticShooterEntity.this.getRandom();
                double x = ParasiticShooterEntity.this.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
                double y = ParasiticShooterEntity.this.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
                double z = ParasiticShooterEntity.this.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
                return new Vec3(x, y, z);
            }
        });
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, target(this, Player.class));
        this.targetSelector.addGoal(4, target(this, HazmatLeaderEntity.class));
        this.targetSelector.addGoal(5, target(this, HazmatGuardEntity.class));
        this.targetSelector.addGoal(6, target(this, HazmatFlamethrowerEntity.class));
        this.targetSelector.addGoal(7, target(this, HazmatHelicopterEntity.class));
        this.targetSelector.addGoal(8, target(this, IronGolem.class));
        this.targetSelector.addGoal(9, target(this, Villager.class));
        this.targetSelector.addGoal(10, target(this, HazmatMedicEntity.class));
        this.targetSelector.addGoal(11, target(this, WanderingTrader.class));
        this.targetSelector.addGoal(12, target(this, ScientistEntity.class));
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.PARASITIC_SHOOTER_AMBIENT.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
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
        return !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
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

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        PoisonedOrbEntity.shoot(this, target);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 90.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.FLYING_SPEED, 0.3);
    }

    private static class ShooterAttackGoal extends Goal {
        private final ParasiticShooterEntity mob;
        private final double speedModifier;
        private final int attackIntervalMin;
        private final int attackIntervalMax;
        private final float attackRadius;
        private final float attackRadiusSqr;
        private LivingEntity target;
        private int attackTime = -1;
        private int seeTime;

        ShooterAttackGoal(ParasiticShooterEntity mob, double speedModifier, int attackIntervalMin, int attackIntervalMax, float attackRadius) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.attackIntervalMin = attackIntervalMin;
            this.attackIntervalMax = attackIntervalMax;
            this.attackRadius = attackRadius;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.isAlive()) {
                this.target = target;
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.attackTime = -1;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                return;
            }
            double distance = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean canSee = this.mob.getSensing().hasLineOfSight(this.target);
            this.seeTime = canSee ? this.seeTime + 1 : 0;
            if (distance <= this.attackRadiusSqr && this.seeTime >= 5) {
                this.mob.getNavigation().stop();
            } else {
                this.mob.getNavigation().moveTo(this.target, this.speedModifier);
            }
            this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (--this.attackTime == 0) {
                if (!canSee) {
                    return;
                }
                float ratio = (float) Math.sqrt(distance) / this.attackRadius;
                this.mob.performRangedAttack(this.target, Mth.clamp(ratio, 0.1F, 1.0F));
                this.attackTime = Mth.floor(ratio * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
            } else if (this.attackTime < 0) {
                this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(distance) / this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
            }
        }
    }
}
