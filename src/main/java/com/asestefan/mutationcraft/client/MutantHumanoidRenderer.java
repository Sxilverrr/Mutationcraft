package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.HazmatFlamethrowerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class MutantHumanoidRenderer<T extends Mob> extends HumanoidMobRenderer<T, HumanoidModel<T>> {
    private final ResourceLocation texture;

    public MutantHumanoidRenderer(EntityRendererProvider.Context context, String texture) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        this.texture = ModUtil.id(texture);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        HumanoidModel.ArmPose pose = entity instanceof HazmatFlamethrowerEntity hazmat && hazmat.isSpraying() && hazmat.isAlive() ? HumanoidModel.ArmPose.BOW_AND_ARROW : HumanoidModel.ArmPose.EMPTY;
        this.model.rightArmPose = entity.isLeftHanded() ? HumanoidModel.ArmPose.EMPTY : pose;
        this.model.leftArmPose = entity.isLeftHanded() ? pose : HumanoidModel.ArmPose.EMPTY;
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.texture;
    }
}
