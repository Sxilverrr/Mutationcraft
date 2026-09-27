package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.entity.AnimatedMutant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MutantGeoRenderer<T extends LivingEntity & AnimatedMutant> extends GeoEntityRenderer<T> {
    private final boolean deathRotation;

    public MutantGeoRenderer(EntityRendererProvider.Context context, String name, float shadowRadius) {
        this(context, name, shadowRadius, true);
    }

    public MutantGeoRenderer(EntityRendererProvider.Context context, String name, float shadowRadius, boolean deathRotation) {
        super(context, new MutantGeoModel<>(name));
        this.shadowRadius = shadowRadius;
        this.deathRotation = deathRotation;
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return this.deathRotation ? super.getDeathMaxRotation(animatable) : 0.0F;
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
