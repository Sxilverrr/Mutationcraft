package com.asestefan.mutationcraft.neoforge;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.client.FlamethrowerClient;
import com.asestefan.mutationcraft.client.FlamethrowerFlameParticle;
import com.asestefan.mutationcraft.client.FlamethrowerScreen;
import com.asestefan.mutationcraft.client.ModEntityRenderers;
import com.asestefan.mutationcraft.init.ModItems;
import com.asestefan.mutationcraft.init.ModMenus;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.init.ModParticles;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@Mod(value = MutationcraftMod.MODID, dist = Dist.CLIENT)
public class MutationcraftNeoForgeClient {
    public MutationcraftNeoForgeClient(IEventBus modBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modBus.addListener(this::onRegisterRenderers);
        modBus.addListener(this::onRegisterScreens);
        modBus.addListener(this::onClientExtensions);
        modBus.addListener(this::onRegisterGuiLayers);
        modBus.addListener(this::onRegisterParticles);
    }

    private void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.FLAMETHROWER.get(), FlamethrowerScreen::new);
    }

    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntityRenderers.register(event::registerEntityRenderer);
    }

    private void onClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerMobEffect(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInGui(MobEffectInstance instance) {
                return false;
            }
        }, ModMobEffects.MUTAGEN_SICKNESS.get());
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                return FlamethrowerClient.armPose(entity, hand, stack);
            }
        }, ModItems.FLAMETHROWER.get());
    }

    private void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.FLAMETHROWER_FLAME.get(), FlamethrowerFlameParticle.Provider::new);
    }

    private void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, ModUtil.id("mutationcraft:flamethrower_overheat"),
                (graphics, delta) -> FlamethrowerClient.renderOverlay(graphics, graphics.guiWidth(), graphics.guiHeight()));
    }
}
