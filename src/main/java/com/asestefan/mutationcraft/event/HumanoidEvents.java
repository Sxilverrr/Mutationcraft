package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.AssimilatedHumanEvolution;
import com.asestefan.mutationcraft.behavior.AssimilatedRoamerAttack;
import com.asestefan.mutationcraft.behavior.AssimilatedVillagerCure;
import com.asestefan.mutationcraft.entity.AssimilatedHumanEntity;
import com.asestefan.mutationcraft.entity.AssimilatedPiglinEntity;
import com.asestefan.mutationcraft.entity.AssimilatedPillagerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedRoamerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedVillagerEntity;
import com.asestefan.mutationcraft.entity.AssimilatedWanderingTraderEntity;
import com.asestefan.mutationcraft.entity.DevelopedRoamerEntity;
import com.asestefan.mutationcraft.entity.HumanStage1Entity;
import com.asestefan.mutationcraft.entity.HumanStage2Entity;
import com.asestefan.mutationcraft.entity.HumanStage3Entity;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;

public final class HumanoidEvents implements EventHandler {
    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker instanceof AssimilatedRoamerEntity roamer) {
            AssimilatedRoamerAttack.assimilated(entity, roamer);
        } else if (attacker instanceof DevelopedRoamerEntity developed) {
            AssimilatedRoamerAttack.developed(entity, developed);
        }
        if (entity instanceof DevelopedRoamerEntity hurtRoamer) {
            AssimilatedRoamerAttack.developedHalfHealth(hurtRoamer);
        }
        if (entity instanceof Player && attacker instanceof AssimilatedWanderingTraderEntity) {
            entity.addEffect(new MobEffectInstance(ModMobEffects.CORROSION.ref(), 600, 0));
        }
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!entity.level().isClientSide() && hasFireAspectWeakness(entity) && source.getEntity() instanceof LivingEntity attacker) {
            int level = ModUtil.enchantmentLevel(Enchantments.FIRE_ASPECT, attacker.getMainHandItem());
            if (level == 1) {
                return amount + 2.0F;
            }
            if (level == 2) {
                return amount + 4.0F;
            }
        }
        return amount;
    }

    @Override
    public void onLivingDeath(LivingEntity entity, DamageSource source) {
        AssimilatedHumanEvolution.onDeath(entity, source);
    }

    @Override
    public boolean onEntityInteract(Player player, Entity target, InteractionHand hand) {
        return AssimilatedVillagerCure.execute(player, target, hand);
    }

    private static boolean hasFireAspectWeakness(LivingEntity entity) {
        return entity instanceof AssimilatedVillagerEntity || entity instanceof AssimilatedPillagerEntity || entity instanceof AssimilatedPiglinEntity
                || entity instanceof AssimilatedHumanEntity || entity instanceof AssimilatedRoamerEntity || entity instanceof DevelopedRoamerEntity
                || entity instanceof HumanStage1Entity || entity instanceof HumanStage2Entity || entity instanceof HumanStage3Entity;
    }
}
