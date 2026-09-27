package com.asestefan.mutationcraft.forge;

import com.asestefan.mutationcraft.client.FlamethrowerClient;
import com.asestefan.mutationcraft.effect.MutagenSicknessMobEffect;
import com.asestefan.mutationcraft.item.FlamethrowerItem;
import com.asestefan.mutationcraft.platform.IPlatformHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public CompoundTag getPersistentData(Entity entity) {
        return entity.getPersistentData();
    }



    @Override
    public void sendVariablesToPlayer(ServerPlayer player, int type, CompoundTag data) {
        ForgeNetwork.sendToPlayer(player, type, data);
    }

    @Override
    public void addDungeonMob(EntityType<?> type, int weight) {
        DungeonHooks.addDungeonMob(type, weight);
    }

    @Override
    public MobEffect createMutagenSicknessEffect() {
        return new MutagenSicknessMobEffect() {
            @Override
            public List<ItemStack> getCurativeItems() {
                return new ArrayList<>();
            }

            @Override
            public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
                consumer.accept(new IClientMobEffectExtensions() {
                    @Override
                    public boolean isVisibleInGui(MobEffectInstance instance) {
                        return false;
                    }
                });
            }
        };
    }

    @Override
    public void registerBrewingRecipe(Supplier<Ingredient> input, Supplier<Ingredient> ingredient, Supplier<ItemStack> output) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new BrewingRecipe(input.get(), ingredient.get(), output.get()))));
    }

    @Override
    public Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Item.Properties properties) {
        return new ForgeSpawnEggItem(type, 0xFFFFFF, 0xFFFFFF, properties);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> factory) {
        return IForgeMenuType.create((id, inventory, buffer) -> factory.apply(id, inventory));
    }

    @Override
    public Item createFlamethrower() {
        return new FlamethrowerItem() {
            @Override
            public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                consumer.accept(new IClientItemExtensions() {
                    @Override
                    public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                        return FlamethrowerClient.armPose(entity, hand, stack);
                    }
                });
            }
        };
    }

    @Override
    public GoalSelector targetSelector(Mob mob) {
        return mob.targetSelector;
    }
}
