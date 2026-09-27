package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.NecroptorBombBurst;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NecroptorBombEntity extends ThrowableItemProjectile {
    private float impactDamage;
    private double knockback;

    public NecroptorBombEntity(EntityType<? extends NecroptorBombEntity> type, Level level) {
        super(type, level);
    }

    public NecroptorBombEntity(Level level, LivingEntity owner) {
        super(ModEntities.NECROPTOR_BOMB.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.NECROPTOR_BOMB.get();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        Entity owner = this.getOwner();
        return super.canHitEntity(entity) && (owner == null || !owner.isAlliedTo(entity));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        if (this.impactDamage > 0.0F) {
            target.hurt(this.damageSources().thrown(this, this.getOwner()), this.impactDamage);
        }
        if (this.knockback > 0.0 && target instanceof LivingEntity living) {
            Vec3 push = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(this.knockback * 0.6);
            if (push.lengthSqr() > 0.0) {
                living.push(push.x, 0.1, push.z);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.isRemoved() && this.level() instanceof ServerLevel level) {
            Vec3 center = result instanceof BlockHitResult block
                    ? Vec3.atBottomCenterOf(block.getBlockPos().relative(block.getDirection()))
                    : this.position();
            NecroptorBombBurst.burst(level, center);
            this.discard();
        }
    }

    public static NecroptorBombEntity throwFrom(LivingEntity shooter, float velocity, float damage, double knockback) {
        Level level = shooter.level();
        NecroptorBombEntity bomb = new NecroptorBombEntity(level, shooter);
        bomb.impactDamage = damage;
        bomb.knockback = knockback;
        bomb.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity, 1.0F);
        level.addFreshEntity(bomb);
        return bomb;
    }
}
