package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.AnimatedMutant;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
//?}

public class MutantGeoModel<T extends AnimatedMutant> extends GeoModel<T> {
    private static final Map<String, Map<String, float[]>> BONE_SCALES = Map.ofEntries(
            Map.entry("assimilated_enderman", Map.of("bone4", new float[]{1.5F, 1.0F, 1.0F}, "bone18", new float[]{1.4F, 1.0F, 1.0F})),
            Map.entry("assimilated_pig", Map.of("Leg3", new float[]{1.0F, 0.7F, 0.8F})),
            Map.entry("assimilated_sheep", Map.of("Sheep", new float[]{1.4F, 1.3F, 1.2F})),
            Map.entry("blood_spike", Map.of("bone", new float[]{1.1F, 0.8F, 1.0F})),
            Map.entry("carnivorae", Map.of("Mouth", new float[]{1.5F, 1.5F, 1.5F})),
            Map.entry("light_hook", Map.of("Tentacle", new float[]{1.5F, 1.6F, 1.5F})),
            Map.entry("medium_hook", Map.of("Tentacle", new float[]{1.7F, 2.2F, 1.9F})),
            Map.entry("heavy_hook", Map.of("Tentacle", new float[]{2.2F, 3.0F, 2.1F})),
            Map.entry("necroptor", Map.of("Reductor", new float[]{0.4F, 0.5F, 0.5F})),
            Map.entry("poisoned_spikes", Map.of("bone", new float[]{1.1F, 0.8F, 1.0F}, "bone2", new float[]{1.0F, 0.7F, 0.8F},
                    "bone3", new float[]{1.0F, 0.8F, 0.9F}, "bone5", new float[]{1.0F, 0.7F, 1.0F})),
            Map.entry("the_intoxicator", Map.of("BossCreature", new float[]{2.3F, 2.1F, 2.1F})));

    private final ResourceLocation model;
    private final ResourceLocation animation;
    private final Map<String, float[]> boneScales;

    public MutantGeoModel(String name) {
        this.model = ModUtil.id(MutationcraftMod.MODID, "geo/" + name + ".geo.json");
        this.animation = ModUtil.id(MutationcraftMod.MODID, "animations/" + name + ".animation.json");
        this.boneScales = BONE_SCALES.getOrDefault(name, Map.of());
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return ModUtil.id(MutationcraftMod.MODID, "textures/entity/" + animatable.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animation;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        this.boneScales.forEach((bone, scale) -> getBone(bone).ifPresent(geoBone -> geoBone.updateScale(scale[0], scale[1], scale[2])));
    }
}
