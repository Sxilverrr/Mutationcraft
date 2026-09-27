package com.asestefan.mutationcraft.enchantment;

//? if <1.21 {
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class MutationBaneEnchantment extends Enchantment {
    public MutationBaneEnchantment(EquipmentSlot... slots) {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, slots);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return this != enchantment && enchantment != Enchantments.SMITE;
    }
}
//?}
