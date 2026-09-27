package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.item.AssimilatedEnderPearlItem;
import com.asestefan.mutationcraft.item.FangStaffItem;
import com.asestefan.mutationcraft.item.MutantArmorItem;
import com.asestefan.mutationcraft.item.MutantWeaponItem;
import com.asestefan.mutationcraft.item.NecroptorBombItem;
import com.asestefan.mutationcraft.item.PoisonedOrbItem;
import com.asestefan.mutationcraft.item.PutridClawItem;
import com.asestefan.mutationcraft.item.TotemOfImmunityItem;
import com.asestefan.mutationcraft.platform.Services;
import com.asestefan.mutationcraft.registry.ModRegistry;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

public final class ModItems {
    public static final ModRegistry<Item> REGISTRY = ModRegistry.create(Registries.ITEM);

    public static final ModRegistry.Entry<Item> PUTRID_BLOCK = block(ModBlocks.PUTRID_BLOCK);
    public static final ModRegistry.Entry<Item> PUTRID_VINE = block(ModBlocks.PUTRID_VINE);
    public static final ModRegistry.Entry<Item> SCRAP_METAL = simple("scrap_metal");
    public static final ModRegistry.Entry<Item> FLAMETHROWER = REGISTRY.register("flamethrower", Services.PLATFORM::createFlamethrower);
    public static final ModRegistry.Entry<Item> PUTRID_BRAIN = simple("putrid_brain");
    public static final ModRegistry.Entry<Item> PUTRID_HEART = simple("putrid_heart");
    public static final ModRegistry.Entry<Item> PUTRID_FLESH = REGISTRY.register("putrid_flesh", PutridClawItem::new);
    public static final ModRegistry.Entry<Item> NECROPTOR_MEMBRANE = simple("necroptor_membrane");
    public static final ModRegistry.Entry<Item> MUTAGEN_SERUM = REGISTRY.register("mutagen_serum", () -> new Item(new Item.Properties().durability(10).rarity(Rarity.COMMON)));
    public static final ModRegistry.Entry<Item> NECROPTOR_BOMB = REGISTRY.register("necroptor_bomb", NecroptorBombItem::new);
    public static final ModRegistry.Entry<Item> ASSIMILATED_ENDER_PEARL = REGISTRY.register("assimilated_ender_pearl", AssimilatedEnderPearlItem::new);
    public static final ModRegistry.Entry<Item> TOTEM_OF_IMMUNITY = REGISTRY.register("totem_of_immunity", TotemOfImmunityItem::new);
    public static final ModRegistry.Entry<Item> ASSIMILATED_FOX_SPAWN_EGG = egg("assimilated_fox_spawn_egg", ModEntities.ASSIMILATED_FOX);
    public static final ModRegistry.Entry<Item> ASSIMILATED_BEAR_SPAWN_EGG = egg("assimilated_bear_spawn_egg", ModEntities.ASSIMILATED_BEAR);
    public static final ModRegistry.Entry<Item> ASSIMILATED_WOLF_SPAWN_EGG = egg("assimilated_wolf_spawn_egg", ModEntities.ASSIMILATED_WOLF);
    public static final ModRegistry.Entry<Item> ASSIMILATED_VILLAGER_SPAWN_EGG = egg("assimilated_villager_spawn_egg", ModEntities.ASSIMILATED_VILLAGER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_HUMAN_SPAWN_EGG = egg("assimilated_human_spawn_egg", ModEntities.ASSIMILATED_HUMAN);
    public static final ModRegistry.Entry<Item> ASSIMILATED_SHEEP_SPAWN_EGG = egg("assimilated_sheep_spawn_egg", ModEntities.ASSIMILATED_SHEEP);
    public static final ModRegistry.Entry<Item> DEVELOPED_ROAMER_SPAWN_EGG = egg("developed_roamer_spawn_egg", ModEntities.DEVELOPED_ROAMER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_HORSE_SPAWN_EGG = egg("assimilated_horse_spawn_egg", ModEntities.ASSIMILATED_HORSE);
    public static final ModRegistry.Entry<Item> ASSIMILATED_DONKEY_SPAWN_EGG = egg("assimilated_donkey_spawn_egg", ModEntities.ASSIMILATED_DONKEY);
    public static final ModRegistry.Entry<Item> ASSIMILATED_ROAMER_SPAWN_EGG = egg("assimilated_roamer_spawn_egg", ModEntities.ASSIMILATED_ROAMER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_COW_SPAWN_EGG = egg("assimilated_cow_spawn_egg", ModEntities.ASSIMILATED_COW);
    public static final ModRegistry.Entry<Item> ASSIMILATED_PIG_SPAWN_EGG = egg("assimilated_pig_spawn_egg", ModEntities.ASSIMILATED_PIG);
    public static final ModRegistry.Entry<Item> ASSIMILATED_PIGLIN_SPAWN_EGG = egg("assimilated_piglin_spawn_egg", ModEntities.ASSIMILATED_PIGLIN);
    public static final ModRegistry.Entry<Item> ASSIMILATED_ENDERMAN_SPAWN_EGG = egg("assimilated_enderman_spawn_egg", ModEntities.ASSIMILATED_ENDERMAN);
    public static final ModRegistry.Entry<Item> ASSIMILATED_WITCH_SPAWN_EGG = egg("assimilated_witch_spawn_egg", ModEntities.ASSIMILATED_WITCH);
    public static final ModRegistry.Entry<Item> ASSIMILATED_SPIDER_SPAWN_EGG = egg("assimilated_spider_spawn_egg", ModEntities.ASSIMILATED_SPIDER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_JOCKEY_SPAWN_EGG = egg("assimilated_jockey_spawn_egg", ModEntities.ASSIMILATED_JOCKEY);
    public static final ModRegistry.Entry<Item> ASSIMILATED_PILLAGER_SPAWN_EGG = egg("assimilated_pillager_spawn_egg", ModEntities.ASSIMILATED_PILLAGER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_WANDERING_TRADER_SPAWN_EGG = egg("assimilated_wandering_trader_spawn_egg", ModEntities.ASSIMILATED_WANDERING_TRADER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_EVOKER_SPAWN_EGG = egg("assimilated_evoker_spawn_egg", ModEntities.ASSIMILATED_EVOKER);
    public static final ModRegistry.Entry<Item> ASSIMILATED_VEX_SPAWN_EGG = egg("assimilated_vex_spawn_egg", ModEntities.ASSIMILATED_VEX);
    public static final ModRegistry.Entry<Item> HUMAN_STAGE_1_SPAWN_EGG = egg("human_stage_1_spawn_egg", ModEntities.HUMAN_STAGE_1);
    public static final ModRegistry.Entry<Item> HUMAN_STAGE_2_SPAWN_EGG = egg("human_stage_2_spawn_egg", ModEntities.HUMAN_STAGE_2);
    public static final ModRegistry.Entry<Item> HUMAN_STAGE_3_SPAWN_EGG = egg("human_stage_3_spawn_egg", ModEntities.HUMAN_STAGE_3);
    public static final ModRegistry.Entry<Item> CARNIVORAE_SPAWN_EGG = egg("carnivorae_spawn_egg", ModEntities.CARNIVORAE);
    public static final ModRegistry.Entry<Item> NECROPTOR_SPAWN_EGG = egg("necroptor_spawn_egg", ModEntities.NECROPTOR);
    public static final ModRegistry.Entry<Item> REDUCTOR_SPAWN_EGG = egg("reductor_spawn_egg", ModEntities.REDUCTOR);
    public static final ModRegistry.Entry<Item> MITER_SPAWN_EGG = egg("miter_spawn_egg", ModEntities.MITER);
    public static final ModRegistry.Entry<Item> HAZMAT_GUARD_SPAWN_EGG = egg("hazmat_guard_spawn_egg", ModEntities.HAZMAT_GUARD);
    public static final ModRegistry.Entry<Item> SCIENTIST_SPAWN_EGG = egg("scientist_spawn_egg", ModEntities.SCIENTIST);
    public static final ModRegistry.Entry<Item> HAZMAT_HELICOPTER_SPAWN_EGG = egg("hazmat_helicopter_spawn_egg", ModEntities.HAZMAT_HELICOPTER);
    public static final ModRegistry.Entry<Item> HAZMAT_LEADER_SPAWN_EGG = egg("hazmat_leader_spawn_egg", ModEntities.HAZMAT_LEADER);
    public static final ModRegistry.Entry<Item> HAZMAT_FLAMETHROWER_SPAWN_EGG = egg("hazmat_flamethrower_spawn_egg", ModEntities.HAZMAT_FLAMETHROWER);
    public static final ModRegistry.Entry<Item> HAZMAT_MEDIC_SPAWN_EGG = egg("hazmat_medic_spawn_egg", ModEntities.HAZMAT_MEDIC);
    public static final ModRegistry.Entry<Item> ASSIMILATED_CREEPER_SPAWN_EGG = egg("assimilated_creeper_spawn_egg", ModEntities.ASSIMILATED_CREEPER);
    public static final ModRegistry.Entry<Item> FLAYER_SPAWN_EGG = egg("flayer_spawn_egg", ModEntities.FLAYER);
    public static final ModRegistry.Entry<Item> PARASITIC_RAM_SPAWN_EGG = egg("parasitic_ram_spawn_egg", ModEntities.PARASITIC_RAM);
    public static final ModRegistry.Entry<Item> PARASITIC_ROLLER_SPAWN_EGG = egg("parasitic_roller_spawn_egg", ModEntities.PARASITIC_ROLLER);
    public static final ModRegistry.Entry<Item> CORROSION_QUEEN_SPAWN_EGG = egg("corrosion_queen_spawn_egg", ModEntities.CORROSION_QUEEN);
    public static final ModRegistry.Entry<Item> HUMAN_HERDER_SPAWN_EGG = egg("human_herder_spawn_egg", ModEntities.HUMAN_HERDER);
    public static final ModRegistry.Entry<Item> RESENTER_SPAWN_EGG = egg("resenter_spawn_egg", ModEntities.RESENTER);
    public static final ModRegistry.Entry<Item> PARASITIC_SHOOTER_SPAWN_EGG = egg("parasitic_shooter_spawn_egg", ModEntities.PARASITIC_SHOOTER);
    public static final ModRegistry.Entry<Item> RUSTED_METAL_HELMET = armor("rusted_metal_helmet", MutantArmorItem.RUSTED_METAL_ARMOR, 42, ArmorItem.Type.HELMET);
    public static final ModRegistry.Entry<Item> RUSTED_METAL_CHESTPLATE = armor("rusted_metal_chestplate", MutantArmorItem.RUSTED_METAL_ARMOR, 42, ArmorItem.Type.CHESTPLATE);
    public static final ModRegistry.Entry<Item> RUSTED_METAL_LEGGINGS = armor("rusted_metal_leggings", MutantArmorItem.RUSTED_METAL_ARMOR, 42, ArmorItem.Type.LEGGINGS);
    public static final ModRegistry.Entry<Item> RUSTED_METAL_BOOTS = armor("rusted_metal_boots", MutantArmorItem.RUSTED_METAL_ARMOR, 42, ArmorItem.Type.BOOTS);
    public static final ModRegistry.Entry<Item> METAL_HELMET = armor("metal_helmet", MutantArmorItem.METAL_ARMOR, 45, ArmorItem.Type.HELMET);
    public static final ModRegistry.Entry<Item> METAL_CHESTPLATE = armor("metal_chestplate", MutantArmorItem.METAL_ARMOR, 45, ArmorItem.Type.CHESTPLATE);
    public static final ModRegistry.Entry<Item> METAL_LEGGINGS = armor("metal_leggings", MutantArmorItem.METAL_ARMOR, 45, ArmorItem.Type.LEGGINGS);
    public static final ModRegistry.Entry<Item> METAL_BOOTS = armor("metal_boots", MutantArmorItem.METAL_ARMOR, 45, ArmorItem.Type.BOOTS);
    public static final ModRegistry.Entry<Item> INFECTED_RAPIER = weapon("infected_rapier", 1, 1.0F, 8.0F, 15, -2.8F, true);
    public static final ModRegistry.Entry<Item> INFECTED_BATTLE_AXE = weapon("infected_battle_axe", 0, 4.0F, 11.0F, 15, -3.1F, true);
    public static final ModRegistry.Entry<Item> INFECTED_NORDIC_AXE = weapon("infected_nordic_axe", 0, 1.0F, 11.0F, 15, -3.1F, false);
    public static final ModRegistry.Entry<Item> INFECTED_POLE_AXE = weapon("infected_pole_axe", 0, 1.0F, 11.0F, 15, -3.1F, false);
    public static final ModRegistry.Entry<Item> INFECTED_HALBERD = weapon("infected_halberd", 0, 1.0F, 11.0F, 14, -3.1F, false);
    public static final ModRegistry.Entry<Item> INFECTED_CAVALRY_SWORD = weapon("infected_cavalry_sword", 0, 1.0F, 8.5F, 14, -2.9F, false);
    public static final ModRegistry.Entry<Item> INFECTED_FLANGED_MACE = weapon("infected_flanged_mace", 0, 1.0F, 8.5F, 14, -2.7F, false);
    public static final ModRegistry.Entry<Item> FANG_STAFF = REGISTRY.register("fang_staff", FangStaffItem::new);
    public static final ModRegistry.Entry<Item> POISONED_ORB = REGISTRY.register("poisoned_orb", PoisonedOrbItem::new);
    public static final ModRegistry.Entry<Item> LIGHT_HOOK_SPAWN_EGG = egg("light_hook_spawn_egg", ModEntities.LIGHT_HOOK);
    public static final ModRegistry.Entry<Item> MEDIUM_HOOK_SPAWN_EGG = egg("medium_hook_spawn_egg", ModEntities.MEDIUM_HOOK);
    public static final ModRegistry.Entry<Item> HEAVY_HOOK_SPAWN_EGG = egg("heavy_hook_spawn_egg", ModEntities.HEAVY_HOOK);

    private static ModRegistry.Entry<Item> simple(String name) {
        return REGISTRY.register(name, () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));
    }

    private static ModRegistry.Entry<Item> block(ModRegistry.Entry<Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static ModRegistry.Entry<Item> egg(String name, Supplier<? extends EntityType<? extends Mob>> type) {
        return REGISTRY.register(name, () -> Services.PLATFORM.createSpawnEgg(type, new Item.Properties()));
    }

    private static ModRegistry.Entry<Item> weapon(String name, int level, float tierSpeed, float damageBonus, int enchantability, float attackSpeed, boolean fireResistant) {
        return REGISTRY.register(name, () -> new MutantWeaponItem(level, tierSpeed, damageBonus, enchantability, attackSpeed, fireResistant));
    }

    //? if >=1.21 {
    /*private static ModRegistry.Entry<Item> armor(String name, ModRegistry.Entry<ArmorMaterial> material, int durabilityMultiplier, ArmorItem.Type type) {
        return REGISTRY.register(name, () -> new MutantArmorItem(material, durabilityMultiplier, type));
    }
    *///?} else {
    private static ModRegistry.Entry<Item> armor(String name, ArmorMaterial material, int durabilityMultiplier, ArmorItem.Type type) {
        return REGISTRY.register(name, () -> new MutantArmorItem(material, durabilityMultiplier, type));
    }
    //?}

    private ModItems() {
    }
}
