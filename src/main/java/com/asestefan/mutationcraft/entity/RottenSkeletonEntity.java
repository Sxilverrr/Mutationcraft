package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

public class RottenSkeletonEntity extends MutantEntity {
    public RottenSkeletonEntity(EntityType<? extends RottenSkeletonEntity> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return loop("idle");
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        if (direct instanceof AbstractArrow || direct instanceof Player || direct instanceof ThrownPotion || direct instanceof AreaEffectCloud) {
            return false;
        }
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.LIGHTNING_BOLT)
                || source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypes.TRIDENT) || source.is(DamageTypes.FALLING_ANVIL)
                || source.is(DamageTypes.DRAGON_BREATH) || source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        super.mobInteract(player, hand);
        rottenSkeletonAwaken(this);
        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    private static void rottenSkeletonAwaken(LivingEntity skeleton) {
        if (!(skeleton.level() instanceof ServerLevel level) || !skeleton.isAlive()) {
            return;
        }
        TheIntoxicatorEntity boss = ModEntities.THE_INTOXICATOR.get().create(level);
        if (boss != null) {
            boss.moveTo(skeleton.getX(), skeleton.getY(), skeleton.getZ(), level.getRandom().nextFloat() * 360.0F, 0.0F);
            ModUtil.finalizeSpawn(boss, level, level.getCurrentDifficultyAt(boss.blockPosition()), MobSpawnType.MOB_SUMMONED);
            level.addFreshEntity(boss);
        }
        skeleton.kill();
    }
}
