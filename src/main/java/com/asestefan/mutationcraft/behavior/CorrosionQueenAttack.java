package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public final class CorrosionQueenAttack {
    private static final EquipmentSlot[] ARMOR = {EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

    public static void corrode(Player player) {
        player.addEffect(new MobEffectInstance(ModMobEffects.CORROSION.ref(), 200, 0));
        for (EquipmentSlot slot : ARMOR) {
            ModUtil.damageEquipment(player, slot, 3);
        }
    }

    public static void onKillPlayer(CorrosionQueenEntity queen) {
        if (queen.getHealth() < queen.getMaxHealth()) {
            queen.setHealth(queen.getMaxHealth());
        }
    }

    private CorrosionQueenAttack() {
    }
}
