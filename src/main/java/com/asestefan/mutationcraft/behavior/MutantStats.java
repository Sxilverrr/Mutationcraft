package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
//? if >=1.21 {
/*import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
*///?} else {
import java.util.UUID;
//?}

public final class MutantStats {
    //? if >=1.21 {
    /*private static final ResourceLocation HEALTH = ModUtil.id("mutationcraft", "config.health");
    private static final ResourceLocation DAMAGE = ModUtil.id("mutationcraft", "config.damage");
    private static final ResourceLocation SPEED = ModUtil.id("mutationcraft", "config.speed");
    *///?} else {
    private static final UUID HEALTH = UUID.fromString("8d2f4a61-3c7e-4b19-a5d0-6e1f2b9c7a01");
    private static final UUID DAMAGE = UUID.fromString("8d2f4a61-3c7e-4b19-a5d0-6e1f2b9c7a02");
    private static final UUID SPEED = UUID.fromString("8d2f4a61-3c7e-4b19-a5d0-6e1f2b9c7a03");
    //?}

    public static void onJoin(Entity entity) {
        if (entity.level().isClientSide() || !(entity instanceof LivingEntity living) || !ModUtil.isMutant(entity)) {
            return;
        }
        boolean full = living.getHealth() >= living.getMaxHealth();
        apply(living, Attributes.MAX_HEALTH, HEALTH, MutationcraftConfig.MUTANT_HEALTH_MULTIPLIER.get());
        apply(living, Attributes.ATTACK_DAMAGE, DAMAGE, MutationcraftConfig.MUTANT_DAMAGE_MULTIPLIER.get());
        apply(living, Attributes.MOVEMENT_SPEED, SPEED, MutationcraftConfig.MUTANT_SPEED_MULTIPLIER.get());
        if (full || living.getHealth() > living.getMaxHealth()) {
            living.setHealth(living.getMaxHealth());
        }
    }

    //? if >=1.21 {
    /*private static void apply(LivingEntity entity, Holder<Attribute> attribute, ResourceLocation id, double multiplier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        instance.removeModifier(id);
        if (multiplier != 1.0) {
            instance.addPermanentModifier(new AttributeModifier(id, multiplier - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }
    *///?} else {
    private static void apply(LivingEntity entity, Attribute attribute, UUID id, double multiplier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        instance.removeModifier(id);
        if (multiplier != 1.0) {
            instance.addPermanentModifier(new AttributeModifier(id, "Mutationcraft config", multiplier - 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
    //?}

    private MutantStats() {
    }
}
