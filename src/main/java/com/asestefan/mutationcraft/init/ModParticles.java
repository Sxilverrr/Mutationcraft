package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public final class ModParticles {
    public static final ModRegistry<ParticleType<?>> REGISTRY = ModRegistry.create(Registries.PARTICLE_TYPE);
    public static final ModRegistry.Entry<SimpleParticleType> FLAMETHROWER_FLAME = REGISTRY.register("flamethrower_flame", () -> new SimpleParticleType(false) {
    });

    private ModParticles() {
    }
}
