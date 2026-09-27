package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;

public final class AnimalHurt {
    public static float fireAspectBonus(DamageSource source, boolean playersOnly) {
        if (!(source.getEntity() instanceof LivingEntity attacker) || playersOnly && !(attacker instanceof Player)) {
            return 0.0F;
        }
        int level = ModUtil.enchantmentLevel(Enchantments.FIRE_ASPECT, attacker.getMainHandItem());
        if (level == 1) {
            return 2.0F;
        }
        if (level == 2) {
            return 4.0F;
        }
        return 0.0F;
    }

    private AnimalHurt() {
    }
}
