package com.asestefan.mutationcraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.ObjIntConsumer;
//? if <1.21 {
import net.minecraft.world.entity.ai.attributes.AttributeMap;
//?}

public class ModMobEffect extends MobEffect {
    private static final ObjIntConsumer<LivingEntity> NONE = (entity, amplifier) -> {
    };
    private final ObjIntConsumer<LivingEntity> tick;
    private final ObjIntConsumer<LivingEntity> start;

    public ModMobEffect(MobEffectCategory category, int color) {
        this(category, color, NONE, NONE);
    }

    public ModMobEffect(MobEffectCategory category, int color, ObjIntConsumer<LivingEntity> tick, ObjIntConsumer<LivingEntity> start) {
        super(category, color);
        this.tick = tick;
        this.start = start;
    }

    public static ModMobEffect ticking(MobEffectCategory category, int color, ObjIntConsumer<LivingEntity> tick) {
        return new ModMobEffect(category, color, tick, NONE);
    }

    public static ModMobEffect onStart(MobEffectCategory category, int color, ObjIntConsumer<LivingEntity> start) {
        return new ModMobEffect(category, color, NONE, start);
    }

    //? if >=1.21 {
    /*@Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        this.tick.accept(entity, amplifier);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        this.start.accept(entity, amplifier);
    }
    *///?} else {
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        this.tick.accept(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);
        this.start.accept(entity, amplifier);
    }
    //?}
}
