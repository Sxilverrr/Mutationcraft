package com.asestefan.mutationcraft.neoforge;

import com.asestefan.mutationcraft.effect.ModMobEffect;
import com.asestefan.mutationcraft.item.FlamethrowerItem;
import com.asestefan.mutationcraft.platform.IPlatformHelper;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public CompoundTag getPersistentData(Entity entity) {
        return entity.getPersistentData();
    }



    @Override
    public void sendVariablesToPlayer(ServerPlayer player, int type, CompoundTag data) {
        NeoForgeNetwork.sendToPlayer(player, type, data);
    }

    @Override
    public void addDungeonMob(EntityType<?> type, int weight) {
    }

    @Override
    public MobEffect createMutagenSicknessEffect() {
        return new ModMobEffect(MobEffectCategory.HARMFUL, -16777216) {
            @Override
            public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance instance) {
                cures.add(EffectCures.PROTECTED_BY_TOTEM);
            }
        };
    }

    @Override
    public void registerBrewingRecipe(Supplier<Ingredient> input, Supplier<Ingredient> ingredient, Supplier<ItemStack> output) {
        NeoForge.EVENT_BUS.addListener((RegisterBrewingRecipesEvent event) -> event.getBuilder().addRecipe(new BrewingRecipe(input.get(), ingredient.get(), output.get())));
    }

    @Override
    public Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Item.Properties properties) {
        return new DeferredSpawnEggItem(type, 0xFFFFFF, 0xFFFFFF, properties);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> factory) {
        return IMenuTypeExtension.create((id, inventory, buffer) -> factory.apply(id, inventory));
    }

    @Override
    public Item createFlamethrower() {
        return new FlamethrowerItem();
    }

    @Override
    public GoalSelector targetSelector(Mob mob) {
        return mob.targetSelector;
    }
}
