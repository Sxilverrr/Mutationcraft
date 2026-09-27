package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModItems;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
//? if >=1.21 {
/*import net.minecraft.world.entity.ai.attributes.Attributes;
*///?}

public class PoisonedOrbEntity extends AbstractArrow implements ItemSupplier {
    //? if >=1.21 {
    /*private int knockback;
    *///?}

    public PoisonedOrbEntity(EntityType<? extends PoisonedOrbEntity> type, Level level) {
        super(type, level);
    }

    public PoisonedOrbEntity(LivingEntity owner, Level level) {
        //? if >=1.21 {
        /*super(ModEntities.POISONED_ORB.get(), owner, level, new ItemStack(ModItems.POISONED_ORB.get()), null);
        *///?} else {
        super(ModEntities.POISONED_ORB.get(), owner, level);
        //?}
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.POISONED_ORB.get());
    }

    //? if >=1.21 {
    /*@Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.POISONED_ORB.get());
    }

    public void setKnockback(int knockback) {
        this.knockback = knockback;
    }
    *///?} else {
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.POISONED_ORB.get());
    }
    //?}

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
        //? if >=1.21 {
        /*if (this.knockback > 0) {
            double resistance = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 push = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(this.knockback * 0.6 * resistance);
            if (push.lengthSqr() > 0.0) {
                entity.push(push.x, 0.1, push.z);
            }
        }
        *///?}
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        Entity owner = this.getOwner();
        return super.canHitEntity(entity) && (owner == null || !owner.isAlliedTo(entity));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide() && result.getEntity() instanceof LivingEntity living && !ModUtil.isMutant(living)) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
            living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 2000, 0));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround) {
            this.discard();
        }
    }

    public static PoisonedOrbEntity shoot(Level level, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        PoisonedOrbEntity orb = new PoisonedOrbEntity(entity, level);
        Vec3 view = entity.getViewVector(1.0F);
        orb.shoot(view.x, view.y, view.z, power * 2.0F, 0.0F);
        orb.setSilent(true);
        orb.setCritArrow(false);
        orb.setBaseDamage(damage);
        orb.setKnockback(knockback);
        level.addFreshEntity(orb);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.MUTANT_TRANSFORM.get(), SoundSource.PLAYERS, 1.0F,
                1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F);
        return orb;
    }

    public static PoisonedOrbEntity shoot(LivingEntity entity, LivingEntity target) {
        Level level = entity.level();
        PoisonedOrbEntity orb = new PoisonedOrbEntity(entity, level);
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        orb.shoot(dx, dy - orb.getY() + Math.hypot(dx, dz) * 0.2F, dz, 2.4F, 12.0F);
        orb.setSilent(true);
        orb.setBaseDamage(5.0);
        orb.setKnockback(2);
        orb.setCritArrow(false);
        level.addFreshEntity(orb);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.MUTANT_TRANSFORM.get(), SoundSource.PLAYERS, 1.0F,
                1.0F / (entity.getRandom().nextFloat() * 0.5F + 1.0F));
        return orb;
    }
}
