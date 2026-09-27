package com.asestefan.mutationcraft.platform;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface IPlatformHelper {
    CompoundTag getPersistentData(Entity entity);



    void sendVariablesToPlayer(ServerPlayer player, int type, CompoundTag data);

    void addDungeonMob(EntityType<?> type, int weight);

    MobEffect createMutagenSicknessEffect();

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> factory);

    Item createFlamethrower();

    Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Item.Properties properties);

    void registerBrewingRecipe(Supplier<Ingredient> input, Supplier<Ingredient> ingredient, Supplier<ItemStack> output);

    GoalSelector targetSelector(Mob mob);
}
