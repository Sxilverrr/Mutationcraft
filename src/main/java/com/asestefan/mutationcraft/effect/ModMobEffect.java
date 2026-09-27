package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
//? if <1.21 {
import net.minecraft.world.entity.ai.attributes.AttributeMap;
//?}

public class ModMobEffect extends MobEffect {
    public ModMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    protected void onTick(LivingEntity entity, int amplifier) {
    }

    protected void onStart(LivingEntity entity, int amplifier) {
    }

    //? if >=1.21 {
    /*@Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        onTick(entity, amplifier);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        onStart(entity, amplifier);
    }
    *///?} else {
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        onTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);
        onStart(entity, amplifier);
    }
    //?}
}
