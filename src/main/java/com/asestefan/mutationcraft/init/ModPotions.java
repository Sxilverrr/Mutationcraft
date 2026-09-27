package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.platform.Services;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
//? if >=1.21 {
/*import net.minecraft.world.item.alchemy.PotionContents;
*///?} else {
import net.minecraft.world.item.alchemy.PotionUtils;
//?}

public final class ModPotions {
    public static final ModRegistry<Potion> REGISTRY = ModRegistry.create(Registries.POTION);

    public static final ModRegistry.Entry<Potion> MUTAGEN_SICKNESS_POTION = REGISTRY.register("mutagen_sickness_potion",
            () -> new Potion(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 3600, 0, false, true)));
    public static final ModRegistry.Entry<Potion> CORROSION_POTION = REGISTRY.register("corrosion_potion",
            () -> new Potion(new MobEffectInstance(ModMobEffects.CORROSION.ref(), 3600, 0, false, true)));
    public static final ModRegistry.Entry<Potion> RAGE_POTION = REGISTRY.register("rage_potion",
            () -> new Potion(new MobEffectInstance(ModMobEffects.RAGE.ref(), 3600, 0, false, true)));
    public static final ModRegistry.Entry<Potion> SEVEREN_POTION = REGISTRY.register("severen_potion",
            () -> new Potion(new MobEffectInstance(ModMobEffects.SEVEREN.ref(), 3600, 0, false, true)));
    public static final ModRegistry.Entry<Potion> SIGN_OF_FIRE_POTION = REGISTRY.register("sign_of_fire_potion",
            () -> new Potion(new MobEffectInstance(ModMobEffects.SIGN_OF_FIRE.ref(), 140, 0, false, true)));

    static {
        Services.PLATFORM.registerBrewingRecipe(() -> Ingredient.of(Items.GLASS_BOTTLE), () -> Ingredient.of(ModItems.ASSIMILATED_ENDER_PEARL.get()), ModPotions::signOfFirePotion);
    }

    public static ItemStack signOfFirePotion() {
        //? if >=1.21 {
        /*return PotionContents.createItemStack(Items.POTION, SIGN_OF_FIRE_POTION.holder());
        *///?} else {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), SIGN_OF_FIRE_POTION.get());
        //?}
    }

    private ModPotions() {
    }
}
