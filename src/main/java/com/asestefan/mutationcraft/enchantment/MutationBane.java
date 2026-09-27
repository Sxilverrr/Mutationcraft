package com.asestefan.mutationcraft.enchantment;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModEnchantments;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public final class MutationBane {
    public static float bonusDamage(LivingEntity target, DamageSource source) {
        if (!(source.getEntity() instanceof LivingEntity attacker) || source.getDirectEntity() != attacker || source.is(DamageTypes.THORNS) || !ModUtil.isMutant(target)) {
            return 0.0F;
        }
        return 2.5F * ModUtil.enchantmentLevel(ModEnchantments.MUTATION_BANE, attacker.getMainHandItem());
    }

    private MutationBane() {
    }
}
