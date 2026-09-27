package com.asestefan.mutationcraft.effect;

import com.asestefan.mutationcraft.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public final class EffectProcedures {
    private static final EquipmentSlot[] ARMOR = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final double STILL = 1.0E-4;
    private static final String BLEED_X = "mutationcraft:bleed_x";
    private static final String BLEED_Z = "mutationcraft:bleed_z";
    private static final String BLEED_TIME = "mutationcraft:bleed_time";

    public static void corrodeArmor(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return;
        }
        RandomSource random = entity.getRandom();
        for (EquipmentSlot slot : ARMOR) {
            if (random.nextDouble() < 0.0025 * (amplifier + 1)) {
                ModUtil.damageEquipment(entity, slot, 6);
            } else if (random.nextDouble() < 0.0006 * (amplifier + 1)) {
                ModUtil.damageEquipment(entity, slot, 8);
            }
        }
    }

    public static void severen(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return;
        }
        RandomSource random = entity.getRandom();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        float scale = 1.0F + amplifier * 0.5F;
        if (r1 <= 0.1) {
            if (r2 <= 0.1) {
                entity.hurt(entity.damageSources().generic(), 2.0F * scale);
            }
        } else if (r1 <= 0.2 && r2 <= 0.1) {
            entity.hurt(entity.damageSources().generic(), 1.0F * scale);
        }
    }

    public static void bleed(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return;
        }
        boolean moving = isMoving(entity);
        RandomSource random = entity.getRandom();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        float extra;
        if (r1 <= 0.15) {
            if (r2 > 0.15) {
                return;
            }
            extra = 2.0F;
        } else if (r1 <= 0.25 && r2 <= 0.25) {
            extra = 1.0F;
        } else {
            return;
        }
        entity.hurt(entity.damageSources().generic(), (moving ? 0.5F + extra : 0.5F) * (1.0F + amplifier * 0.5F));
    }

    public static void signOfFire(LivingEntity entity) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (Entity target : entity.level().getEntitiesOfClass(Entity.class, new AABB(entity.position(), entity.position()).inflate(2.0))) {
            ModUtil.setOnFire(target, 7);
        }
    }

    private static boolean isMoving(LivingEntity entity) {
        if (!(entity instanceof Player)) {
            return entity.getDeltaMovement().horizontalDistanceSqr() > STILL;
        }
        CompoundTag data = ModUtil.data(entity);
        long now = entity.level().getGameTime();
        boolean known = data.contains(BLEED_X) && data.getLong(BLEED_TIME) == now - 1;
        double dx = entity.getX() - data.getDouble(BLEED_X);
        double dz = entity.getZ() - data.getDouble(BLEED_Z);
        data.putDouble(BLEED_X, entity.getX());
        data.putDouble(BLEED_Z, entity.getZ());
        data.putLong(BLEED_TIME, now);
        return known && dx * dx + dz * dz > STILL;
    }

    private EffectProcedures() {
    }
}
