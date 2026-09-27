package com.asestefan.mutationcraft.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
//? if >=1.21 {
/*import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import net.minecraft.resources.ResourceKey;
*///?} else {
import com.asestefan.mutationcraft.enchantment.MutationBaneEnchantment;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
//?}

public final class ModEnchantments {
    //? if >=1.21 {
    /*public static final ResourceKey<Enchantment> MUTATION_BANE = ResourceKey.create(Registries.ENCHANTMENT, ModUtil.id(MutationcraftMod.MODID, "mutation_bane"));
    *///?} else {
    public static final ModRegistry<Enchantment> REGISTRY = ModRegistry.create(Registries.ENCHANTMENT);

    public static final ModRegistry.Entry<Enchantment> MUTATION_BANE = REGISTRY.register("mutation_bane", () -> new MutationBaneEnchantment(EquipmentSlot.MAINHAND));
    //?}

    public static void init() {
    }

    private ModEnchantments() {
    }
}
