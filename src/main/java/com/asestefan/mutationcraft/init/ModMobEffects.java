package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.effect.EffectProcedures;
import com.asestefan.mutationcraft.effect.ModMobEffect;
import com.asestefan.mutationcraft.effect.RageMobEffect;
import com.asestefan.mutationcraft.platform.Services;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class ModMobEffects {
    public static final ModRegistry<MobEffect> REGISTRY = ModRegistry.create(Registries.MOB_EFFECT);

    public static final ModRegistry.Entry<MobEffect> MUTAGEN_SICKNESS = REGISTRY.register("mutagen_sickness", Services.PLATFORM::createMutagenSicknessEffect);
    public static final ModRegistry.Entry<MobEffect> MAGIC_EXHAUSTION = REGISTRY.register("magic_exhaustion", () -> new ModMobEffect(MobEffectCategory.HARMFUL, -39424));
    public static final ModRegistry.Entry<MobEffect> CORROSION = REGISTRY.register("corrosion", () -> ModMobEffect.onStart(MobEffectCategory.HARMFUL, -14286591, EffectProcedures::corrodeArmor));
    public static final ModRegistry.Entry<MobEffect> RAGE = REGISTRY.register("rage", RageMobEffect::new);
    public static final ModRegistry.Entry<MobEffect> SEVEREN = REGISTRY.register("severen", () -> ModMobEffect.ticking(MobEffectCategory.HARMFUL, -16764160, EffectProcedures::severen));
    public static final ModRegistry.Entry<MobEffect> BLEEDING = REGISTRY.register("bleeding", () -> ModMobEffect.ticking(MobEffectCategory.HARMFUL, -8388608, EffectProcedures::bleed));
    public static final ModRegistry.Entry<MobEffect> SIGN_OF_FIRE = REGISTRY.register("sign_of_fire", () -> ModMobEffect.onStart(MobEffectCategory.HARMFUL, -6737152, (entity, amplifier) -> EffectProcedures.signOfFire(entity)));

    private ModMobEffects() {
    }
}
