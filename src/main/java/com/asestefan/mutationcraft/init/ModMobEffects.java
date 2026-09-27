package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.effect.BleedingMobEffect;
import com.asestefan.mutationcraft.effect.CorrosionMobEffect;
import com.asestefan.mutationcraft.effect.ModMobEffect;
import com.asestefan.mutationcraft.effect.RageMobEffect;
import com.asestefan.mutationcraft.effect.SeverenMobEffect;
import com.asestefan.mutationcraft.effect.SignOfFireMobEffect;
import com.asestefan.mutationcraft.platform.Services;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class ModMobEffects {
    public static final ModRegistry<MobEffect> REGISTRY = ModRegistry.create(Registries.MOB_EFFECT);

    public static final ModRegistry.Entry<MobEffect> MUTAGEN_SICKNESS = REGISTRY.register("mutagen_sickness", Services.PLATFORM::createMutagenSicknessEffect);
    public static final ModRegistry.Entry<MobEffect> MAGIC_EXHAUSTION = REGISTRY.register("magic_exhaustion", () -> new ModMobEffect(MobEffectCategory.HARMFUL, -39424));
    public static final ModRegistry.Entry<MobEffect> CORROSION = REGISTRY.register("corrosion", CorrosionMobEffect::new);
    public static final ModRegistry.Entry<MobEffect> RAGE = REGISTRY.register("rage", RageMobEffect::new);
    public static final ModRegistry.Entry<MobEffect> SEVEREN = REGISTRY.register("severen", SeverenMobEffect::new);
    public static final ModRegistry.Entry<MobEffect> BLEEDING = REGISTRY.register("bleeding", BleedingMobEffect::new);
    public static final ModRegistry.Entry<MobEffect> SIGN_OF_FIRE = REGISTRY.register("sign_of_fire", SignOfFireMobEffect::new);

    private ModMobEffects() {
    }
}
