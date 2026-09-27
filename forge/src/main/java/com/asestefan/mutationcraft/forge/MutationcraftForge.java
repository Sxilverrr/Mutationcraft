package com.asestefan.mutationcraft.forge;

import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.client.FlamethrowerClient;
import com.asestefan.mutationcraft.client.FlamethrowerFlameParticle;
import com.asestefan.mutationcraft.client.FlamethrowerScreen;
import com.asestefan.mutationcraft.client.ModEntityRenderers;
import com.asestefan.mutationcraft.command.ModCommands;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.event.ModEvents;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMenus;
import com.asestefan.mutationcraft.init.ModParticles;
import com.asestefan.mutationcraft.network.ModVariables;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(MutationcraftMod.MODID)
public class MutationcraftForge {
    public MutationcraftForge() {
        MutationcraftMod.init();
        ForgeConfigSpec.Builder config = new ForgeConfigSpec.Builder();
        String section = null;
        for (MutationcraftConfig.Entry entry : MutationcraftConfig.ENTRIES) {
            if (!entry.section.equals(section)) {
                if (section != null) {
                    config.pop();
                }
                section = entry.section;
                config.translation("mutationcraft.configuration." + section).push(section);
            }
            config.comment(entry.comment).translation("mutationcraft.configuration." + entry.name);
            if (entry instanceof MutationcraftConfig.Toggle toggle) {
                ForgeConfigSpec.BooleanValue value = config.define(toggle.name, toggle.defaultValue);
                toggle.bind(value::get);
            } else if (entry instanceof MutationcraftConfig.Number number) {
                ForgeConfigSpec.DoubleValue value = config.defineInRange(number.name, number.defaultValue, number.min, number.max);
                number.bind(value::get);
            }
        }
        config.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config.build());
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onRegister);
        modBus.addListener(this::onAttributes);
        modBus.addListener(this::onSpawnPlacements);
        modBus.addListener(this::onCommonSetup);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(this::onRegisterRenderers);
            modBus.addListener(this::onClientSetup);
            modBus.addListener(this::onRegisterOverlays);
            modBus.addListener(this::onRegisterParticles);
        }

        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(this::onLivingAttack);
        bus.addListener(this::onLivingHurt);
        bus.addListener(this::onLivingDeath);
        bus.addListener(this::onLivingDrops);
        bus.addListener(this::onChangeTarget);
        bus.addListener(this::onLivingTick);
        bus.addListener(this::onPlayerTick);
        bus.addListener(this::onLevelTick);
        bus.addListener(this::onServerTick);
        bus.addListener(this::onEntityJoin);
        bus.addListener(this::onEntityInteract);
        bus.addListener(this::onPlayerLoggedIn);
        bus.addListener(this::onRegisterCommands);
        bus.addListener(EventPriority.LOWEST, this::onEffectExpired);
        bus.addListener(this::onEffectAdded);
        bus.addListener(this::onEffectApplicable);
        ForgeNetwork.init();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenus.FLAMETHROWER.get(), FlamethrowerScreen::new));
    }

    private void onRegister(RegisterEvent event) {
        for (ModRegistry<?> registry : ModRegistry.ALL) {
            register(event, registry);
        }
    }

    private static <T> void register(RegisterEvent event, ModRegistry<T> registry) {
        if (event.getRegistryKey().equals(registry.key())) {
            registry.registerAll((id, supplier) -> event.register(registry.key(), id, supplier));
        }
    }

    private void onAttributes(EntityAttributeCreationEvent event) {
        ModEntities.registerAttributes(event::put);
    }

    private void onSpawnPlacements(SpawnPlacementRegisterEvent event) {
        ModEntities.registerSpawnPlacements(new ModEntities.SpawnPlacementRegistrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
                event.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate, SpawnPlacementRegisterEvent.Operation.REPLACE);
            }
        });
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModEntities::commonSetup);
    }

    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntityRenderers.register(event::registerEntityRenderer);
    }

    private void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.FLAMETHROWER_FLAME.get(), FlamethrowerFlameParticle.Provider::new);
    }

    private void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "flamethrower_overheat",
                (gui, graphics, partialTick, width, height) -> FlamethrowerClient.renderOverlay(graphics, width, height));
    }

    private void onLivingAttack(LivingAttackEvent event) {
        ModEvents.onLivingAttack(event.getEntity(), event.getSource());
    }

    private void onLivingHurt(LivingHurtEvent event) {
        event.setAmount(ModEvents.onLivingHurt(event.getEntity(), event.getSource(), event.getAmount()));
    }

    private void onLivingDeath(LivingDeathEvent event) {
        ModEvents.onLivingDeath(event.getEntity(), event.getSource());
    }

    private void onLivingDrops(LivingDropsEvent event) {
        if (ModEvents.dropsDisabled(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    private void onChangeTarget(LivingChangeTargetEvent event) {
        ModEvents.onChangeTarget(event.getEntity(), event.getNewTarget());
    }

    private void onLivingTick(LivingEvent.LivingTickEvent event) {
        ModEvents.onLivingTick(event.getEntity());
    }

    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ModEvents.onPlayerTick(event.player);
        }
    }

    private void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ModEvents.onLevelTick(event.level);
        }
    }

    private void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ModEvents.onServerTick();
        }
    }

    private void onEntityJoin(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
        ModEvents.onEntityJoin(event.getEntity());
    }

    private void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (ModEvents.onEntityInteract(event.getEntity(), event.getTarget(), event.getHand())) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModVariables.onPlayerLoggedIn(player);
        }
    }

    private void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!ModEvents.canApplyEffect(event.getEntity(), event.getEffectInstance().getEffect())) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onEffectAdded(MobEffectEvent.Added event) {
        ModEvents.onEffectAdded(event.getEntity(), event.getEffectInstance());
    }

    private void onEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance instance = event.getEffectInstance();
        if (instance != null) {
            ModEvents.onEffectExpired(event.getEntity(), instance);
        }
    }
}
