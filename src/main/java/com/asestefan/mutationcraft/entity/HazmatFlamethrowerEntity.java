package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.HazmatFlamethrowerDeath;
import com.asestefan.mutationcraft.behavior.HazmatFlamethrowerSpray;
import com.asestefan.mutationcraft.behavior.HazmatTargets;
import com.asestefan.mutationcraft.client.FlamethrowerClient;
import com.asestefan.mutationcraft.init.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HazmatFlamethrowerEntity extends HazmatMob {
    public static final EntityDataAccessor<Boolean> SPRAYING = SynchedEntityData.defineId(HazmatFlamethrowerEntity.class, EntityDataSerializers.BOOLEAN);

    public HazmatFlamethrowerEntity(EntityType<? extends HazmatFlamethrowerEntity> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLAMETHROWER.get()));
    }

    @Override
    protected boolean formerMonster() {
        return true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SprayGoal());
        this.goalSelector.addGoal(1, MutantEntity.meleeGoal(this, 1.2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        HazmatTargets.add(this, this.targetSelector, 6);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.level() instanceof ServerLevel level) {
            HazmatFlamethrowerDeath.explode(level, this.getX(), this.getY(), this.getZ());
        }
    }

    //? if >=1.21 {
    /*@Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPRAYING, false);
    }
    *///?} else {
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPRAYING, false);
    }
    //?}

    public boolean isSpraying() {
        return this.entityData.get(SPRAYING);
    }

    public void setSpraying(boolean spraying) {
        this.entityData.set(SPRAYING, spraying);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && !this.isAlive() && this.isSpraying()) {
            this.setSpraying(false);
        }
        if (this.level().isClientSide() && this.isAlive() && this.isSpraying()) {
            FlamethrowerClient.playLoop(this, () -> this.isAlive() && this.isSpraying());
        }
    }

    private class SprayGoal extends Goal {
        private int cooldown;
        private int burst;

        @Override
        public boolean canUse() {
            LivingEntity target = HazmatFlamethrowerEntity.this.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void stop() {
            this.burst = 0;
            HazmatFlamethrowerEntity.this.setSpraying(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            HazmatFlamethrowerEntity self = HazmatFlamethrowerEntity.this;
            LivingEntity target = self.getTarget();
            if (target == null) {
                return;
            }
            if (this.burst > 0) {
                if (!HazmatFlamethrowerSpray.canBurst(self, target)) {
                    this.burst = 0;
                    this.cooldown = 20;
                    self.setSpraying(false);
                    return;
                }
                HazmatFlamethrowerSpray.tick(self, target, HazmatFlamethrowerSpray.BURST_TICKS - this.burst);
                if (--this.burst == 0) {
                    this.cooldown = 60 + self.getRandom().nextInt(21);
                    self.setSpraying(false);
                }
            } else if (--this.cooldown <= 0 && HazmatFlamethrowerSpray.canBurst(self, target)) {
                this.burst = HazmatFlamethrowerSpray.BURST_TICKS;
                self.setSpraying(true);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.ARMOR_TOUGHNESS, 3.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }
}
