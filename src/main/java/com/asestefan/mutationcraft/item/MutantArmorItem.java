package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.init.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
//? if >=1.21 {
/*import com.asestefan.mutationcraft.registry.ModRegistry;
import java.util.EnumMap;
import java.util.List;
import net.minecraft.core.registries.Registries;
*///?} else {
import net.minecraft.sounds.SoundEvent;
//?}

public class MutantArmorItem extends ArmorItem {
    //? if >=1.21 {
    /*public static final ModRegistry<ArmorMaterial> MATERIALS = ModRegistry.create(Registries.ARMOR_MATERIAL);
    public static final ModRegistry.Entry<ArmorMaterial> RUSTED_METAL_ARMOR = MATERIALS.register("rusted_metal", () -> material("rusted_metal", new int[]{4, 6, 9, 3}));
    public static final ModRegistry.Entry<ArmorMaterial> METAL_ARMOR = MATERIALS.register("metal", () -> material("metal", new int[]{4, 7, 9, 4}));

    public MutantArmorItem(ModRegistry.Entry<ArmorMaterial> material, int durabilityMultiplier, Type type) {
        super(material.holder(), type, new Properties().durability(type.getDurability(durabilityMultiplier)));
    }

    private static ArmorMaterial material(String name, int[] defense) {
        EnumMap<Type, Integer> map = new EnumMap<>(Type.class);
        map.put(Type.BOOTS, defense[0]);
        map.put(Type.LEGGINGS, defense[1]);
        map.put(Type.CHESTPLATE, defense[2]);
        map.put(Type.HELMET, defense[3]);
        return new ArmorMaterial(map, 9, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(ModItems.SCRAP_METAL.get()),
                List.of(new ArmorMaterial.Layer(ModUtil.id(MutationcraftMod.MODID, name))), 4.0F, 0.2F);
    }
    *///?} else {
    public static final ArmorMaterial RUSTED_METAL_ARMOR = new Material("rusted_metal", 42, new int[]{4, 6, 9, 3});
    public static final ArmorMaterial METAL_ARMOR = new Material("metal", 45, new int[]{4, 7, 9, 4});

    public MutantArmorItem(ArmorMaterial material, int durabilityMultiplier, Type type) {
        super(material, type, new Properties());
    }

    private record Material(String name, int durabilityMultiplier, int[] defense) implements ArmorMaterial {
        private static final int[] DURABILITY = {13, 15, 16, 11};

        private static int index(Type type) {
            return switch (type) {
                case BOOTS -> 0;
                case LEGGINGS -> 1;
                case CHESTPLATE -> 2;
                case HELMET -> 3;
            };
        }

        @Override
        public int getDurabilityForType(Type type) {
            return DURABILITY[index(type)] * durabilityMultiplier;
        }

        @Override
        public int getDefenseForType(Type type) {
            return defense[index(type)];
        }

        @Override
        public int getEnchantmentValue() {
            return 9;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.SCRAP_METAL.get());
        }

        @Override
        public String getName() {
            return ModUtil.id(MutationcraftMod.MODID, name).toString();
        }

        @Override
        public float getToughness() {
            return 4.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.2F;
        }
    }
    //?}
}
