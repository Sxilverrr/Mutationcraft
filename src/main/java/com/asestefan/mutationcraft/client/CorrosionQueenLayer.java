package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CorrosionQueenLayer extends GeoRenderLayer<CorrosionQueenEntity> {
    private static final ResourceLocation LAYER = ModUtil.id(MutationcraftMod.MODID, "textures/entity/corrosion_queen_glow.png");

    public CorrosionQueenLayer(GeoRenderer<CorrosionQueenEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, CorrosionQueenEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType eyes = RenderType.eyes(LAYER);
        //? if >=1.21 {
        /*getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, eyes, bufferSource.getBuffer(eyes), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        *///?} else {
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, eyes, bufferSource.getBuffer(eyes), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //?}
    }
}
