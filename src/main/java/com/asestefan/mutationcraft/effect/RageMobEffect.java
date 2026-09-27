package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RageMobEffect extends ModMobEffect {
    public RageMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -13891833);
        //? if >=1.21 {
        /*this.addAttributeModifier(Attributes.ATTACK_DAMAGE, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("mutationcraft", "effect.rage.damage"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("mutationcraft", "effect.rage.speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        *///?} else {
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "5c1e0a3e-7b2d-4c61-9f0e-3a8b6d2f1c01", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "5c1e0a3e-7b2d-4c61-9f0e-3a8b6d2f1c02", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        //?}
    }
}
