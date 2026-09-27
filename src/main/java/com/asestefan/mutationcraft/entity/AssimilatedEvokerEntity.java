package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
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

public class AssimilatedEvokerEntity extends MutantEntity {
    private static final String TOTEM_USED = "mutationcraft:totem_used";
    private DamageSource pendingDamage;

    public AssimilatedEvokerEntity(EntityType<? extends AssimilatedEvokerEntity> type, Level level) {
        super(type, level);
        this.xpReward = 11;
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("walk2") : loop("idle2");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.addLeapGoal(0.5F);
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
        this.addStandardTargets(8);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ASSIMILATED_EVOKER_AMBIENT.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.WITHER_HURT;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.pendingDamage = source;
        try {
            return super.hurt(source, amount);
        } finally {
            this.pendingDamage = null;
        }
    }

    @Override
    public void setHealth(float health) {
        if (health <= 0.0F && this.canUseTotem()) {
            ModUtil.data(this).putBoolean(TOTEM_USED, true);
            super.setHealth(1.0F);
            this.removeAllEffects();
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            this.level().broadcastEntityEvent(this, (byte) 35);
            return;
        }
        super.setHealth(health);
    }

    private boolean canUseTotem() {
        return this.pendingDamage != null && !this.level().isClientSide() && this.isAlive()
                && !this.pendingDamage.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !ModUtil.data(this).getBoolean(TOTEM_USED);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.ARMOR, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6);
    }
}
