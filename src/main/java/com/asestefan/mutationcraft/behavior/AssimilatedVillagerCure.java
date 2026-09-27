package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModItems;
import com.asestefan.mutationcraft.init.ModSounds;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class AssimilatedVillagerCure {
    private static Map<EntityType<?>, EntityType<? extends Mob>> cures;

    private static Map<EntityType<?>, EntityType<? extends Mob>> cures() {
        if (cures == null) {
            Map<EntityType<?>, EntityType<? extends Mob>> map = new HashMap<>();
            map.put(ModEntities.ASSIMILATED_VILLAGER.get(), EntityType.VILLAGER);
            map.put(ModEntities.ASSIMILATED_WANDERING_TRADER.get(), EntityType.WANDERING_TRADER);
            map.put(ModEntities.ASSIMILATED_COW.get(), EntityType.COW);
            map.put(ModEntities.ASSIMILATED_BEAR.get(), EntityType.POLAR_BEAR);
            map.put(ModEntities.ASSIMILATED_FOX.get(), EntityType.FOX);
            map.put(ModEntities.ASSIMILATED_PIG.get(), EntityType.PIG);
            map.put(ModEntities.ASSIMILATED_SHEEP.get(), EntityType.SHEEP);
            map.put(ModEntities.ASSIMILATED_WOLF.get(), EntityType.WOLF);
            map.put(ModEntities.ASSIMILATED_HORSE.get(), EntityType.HORSE);
            map.put(ModEntities.ASSIMILATED_DONKEY.get(), EntityType.DONKEY);
            cures = map;
        }
        return cures;
    }

    public static boolean execute(Player player, Entity target, InteractionHand hand) {
        if (player.level().isClientSide() || hand != InteractionHand.MAIN_HAND || !(target instanceof Mob mob) || !mob.isAlive()) {
            return false;
        }
        EntityType<? extends Mob> cured = cures().get(mob.getType());
        if (cured == null || !player.getMainHandItem().is(ModItems.MUTAGEN_SERUM.get())) {
            return false;
        }
        if (!player.getAbilities().instabuild) {
            ModUtil.damageEquipment(player, EquipmentSlot.MAINHAND, 1);
        }
        if (MutantConversion.convert(mob, cured, ModSounds.MUTANT_TRANSFORM.get()) != null) {
            mob.discard();
        }
        return true;
    }

    private AssimilatedVillagerCure() {
    }
}
