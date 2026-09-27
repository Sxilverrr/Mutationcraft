package com.asestefan.mutationcraft.init;

import net.minecraft.world.item.SpawnEggItem;
import com.asestefan.mutationcraft.registry.ModRegistry;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ModTabs {
    public static final ModRegistry<CreativeModeTab> REGISTRY = ModRegistry.create(Registries.CREATIVE_MODE_TAB);

    public static final ModRegistry.Entry<CreativeModeTab> TAB_OZUL_OUTBREAK = REGISTRY.register("mutationcraft", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.mutationcraft.mutationcraft"))
            .icon(() -> new ItemStack(ModItems.PUTRID_HEART.get()))
            .displayItems((parameters, output) -> {
                for (ModRegistry.Entry<? extends Item> entry : ModItems.REGISTRY.entries()) {
                    if (entry.get() instanceof SpawnEggItem) {
                        output.accept(entry.get());
                    }
                }
            })
            .build());

    public static final ModRegistry.Entry<CreativeModeTab> TAB_OZUL_FEATURES = REGISTRY.register("mutationcraft_features", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.mutationcraft.mutationcraft_features"))
            .icon(() -> new ItemStack(ModItems.PUTRID_FLESH.get()))
            .displayItems((parameters, output) -> accept(output, List.of(ModItems.PUTRID_BLOCK, ModItems.PUTRID_VINE, ModItems.SCRAP_METAL, ModItems.FLAMETHROWER,
                    ModItems.PUTRID_BRAIN, ModItems.PUTRID_HEART, ModItems.PUTRID_FLESH, ModItems.NECROPTOR_MEMBRANE, ModItems.MUTAGEN_SERUM, ModItems.NECROPTOR_BOMB,
                    ModItems.ASSIMILATED_ENDER_PEARL, ModItems.TOTEM_OF_IMMUNITY)))
            .build());

    public static final ModRegistry.Entry<CreativeModeTab> TAB_MUTATIONCRAFT_WEAPONRY = REGISTRY.register("mutationcraft_weaponry", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.mutationcraft.mutationcraft_weaponry"))
            .icon(() -> new ItemStack(ModItems.NECROPTOR_BOMB.get()))
            .displayItems((parameters, output) -> accept(output, List.of(ModItems.RUSTED_METAL_HELMET, ModItems.RUSTED_METAL_CHESTPLATE,
                    ModItems.RUSTED_METAL_LEGGINGS, ModItems.RUSTED_METAL_BOOTS, ModItems.METAL_HELMET, ModItems.METAL_CHESTPLATE,
                    ModItems.METAL_LEGGINGS, ModItems.METAL_BOOTS, ModItems.INFECTED_RAPIER, ModItems.INFECTED_BATTLE_AXE, ModItems.INFECTED_NORDIC_AXE, ModItems.INFECTED_POLE_AXE,
                    ModItems.INFECTED_HALBERD, ModItems.INFECTED_CAVALRY_SWORD, ModItems.INFECTED_FLANGED_MACE, ModItems.FANG_STAFF)))
            .build());

    private static void accept(CreativeModeTab.Output output, List<Supplier<Item>> items) {
        for (Supplier<Item> item : items) {
            output.accept(item.get());
        }
    }

    private ModTabs() {
    }
}
