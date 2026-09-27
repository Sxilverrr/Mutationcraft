package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.init.ModItems;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
//? if >=1.21 {
/*import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
*///?}

public class MutantWeaponItem extends SwordItem {
    public MutantWeaponItem(int level, float tierSpeed, float damageBonus, int enchantability, float attackSpeed, boolean fireResistant) {
        this(new MutantTier(level, tierSpeed, damageBonus, enchantability), attackSpeed, fireResistant);
    }

    private MutantWeaponItem(Tier tier, float attackSpeed, boolean fireResistant) {
        //? if >=1.21 {
        /*super(tier, properties(fireResistant).attributes(SwordItem.createAttributes(tier, 3, attackSpeed)));
        *///?} else {
        super(tier, 3, attackSpeed, properties(fireResistant));
        //?}
    }

    private static Properties properties(boolean fireResistant) {
        Properties properties = new Properties();
        return fireResistant ? properties.fireResistant() : properties;
    }

    private record MutantTier(int level, float tierSpeed, float damageBonus, int enchantability) implements Tier {
        @Override
        public int getUses() {
            return 1000;
        }

        @Override
        public float getSpeed() {
            return tierSpeed;
        }

        @Override
        public float getAttackDamageBonus() {
            return damageBonus;
        }

        //? if >=1.21 {
        /*@Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return level >= 1 ? BlockTags.INCORRECT_FOR_STONE_TOOL : BlockTags.INCORRECT_FOR_WOODEN_TOOL;
        }
        *///?} else {
        @Override
        public int getLevel() {
            return level;
        }
        //?}

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.SCRAP_METAL.get());
        }
    }
}
