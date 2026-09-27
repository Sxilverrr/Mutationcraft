package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.MiterEntity;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MiterRenderer extends MobRenderer<MiterEntity, SilverfishModel<MiterEntity>> {
    private static final ResourceLocation TEXTURE = ModUtil.id(MutationcraftMod.MODID, "textures/entity/miter.png");

    public MiterRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverfishModel<>(context.bakeLayer(ModelLayers.SILVERFISH)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(MiterEntity entity) {
        return TEXTURE;
    }
}
