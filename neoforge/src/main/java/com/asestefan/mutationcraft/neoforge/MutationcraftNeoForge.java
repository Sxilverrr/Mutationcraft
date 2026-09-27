package com.asestefan.mutationcraft.neoforge;

import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.command.ModCommands;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.event.ModEvents;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.network.ModVariables;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(MutationcraftMod.MODID)
public class MutationcraftNeoForge {
    public MutationcraftNeoForge(IEventBus modBus, ModContainer container) {
        MutationcraftMod.init();
        ModConfigSpec.Builder config = new ModConfigSpec.Builder();
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
                ModConfigSpec.BooleanValue value = config.define(toggle.name, toggle.defaultValue);
                toggle.bind(value::get);
            } else if (entry instanceof MutationcraftConfig.Number number) {
                ModConfigSpec.DoubleValue value = config.defineInRange(number.name, number.defaultValue, number.min, number.max);
                number.bind(value::get);
            }
        }
        config.pop();
        container.registerConfig(ModConfig.Type.COMMON, config.build());
        modBus.addListener(this::onRegister);
        modBus.addListener(this::onAttributes);
        modBus.addListener(this::onSpawnPlacements);
        modBus.addListener(this::onCommonSetup);
        modBus.addListener(NeoForgeNetwork::register);

        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(this::onIncomingDamage);
        bus.addListener(this::onLivingDeath);
        bus.addListener(this::onLivingDrops);
        bus.addListener(this::onChangeTarget);
        bus.addListener(this::onEntityTick);
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

    private void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        ModEntities.registerSpawnPlacements(new ModEntities.SpawnPlacementRegistrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
                event.register(type, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
            }
        });
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModEntities::commonSetup);
    }

    private void onIncomingDamage(LivingIncomingDamageEvent event) {
        ModEvents.onLivingAttack(event.getEntity(), event.getSource());
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
        ModEvents.onChangeTarget(event.getEntity(), event.getNewAboutToBeSetTarget());
    }

    private void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living) {
            ModEvents.onLivingTick(living);
        }
    }

    private void onPlayerTick(PlayerTickEvent.Post event) {
        ModEvents.onPlayerTick(event.getEntity());
    }

    private void onLevelTick(LevelTickEvent.Post event) {
        ModEvents.onLevelTick(event.getLevel());
    }

    private void onServerTick(ServerTickEvent.Post event) {
        ModEvents.onServerTick();
    }

    private void onEntityJoin(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
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
        if (!ModEvents.canApplyEffect(event.getEntity(), event.getEffectInstance().getEffect().value())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
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
