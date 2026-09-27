package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class BloodSpikeEntity extends MutantEntity {
    private static final int LIFETIME = 300;
    private static final String AGE_KEY = "SpikeAge";
    private static final String TOUCH_KEY = "mutationcraft:blood_spike_touch";
    private int age;

    public BloodSpikeEntity(EntityType<? extends BloodSpikeEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected String defaultTexture() {
        return "blood_spike";
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return loop("static pose");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.goalSelector.addGoal(2, new FloatGoal(this));
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    public SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        if (direct instanceof AbstractArrow || direct instanceof Player || direct instanceof ThrownPotion || direct instanceof AreaEffectCloud
                || source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.LIGHTNING_BOLT)
                || source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypes.TRIDENT) || source.is(DamageTypes.FALLING_ANVIL)
                || source.is(DamageTypes.DRAGON_BREATH) || source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level() instanceof ServerLevel level && ++this.age >= LIFETIME && this.isAlive()) {
            level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 1.0, 3.0, 1.0, 1.0);
            this.discard();
        }
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        if (this.level().isClientSide() || !this.isAlive() || !ModUtil.tryCooldown(player, TOUCH_KEY, 20)) {
            return;
        }
        player.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING.ref(), 100, 0));
        this.level().explode(null, this.getX(), this.getY(), this.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
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

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(AGE_KEY, this.age);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.age = tag.getInt(AGE_KEY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }
}
