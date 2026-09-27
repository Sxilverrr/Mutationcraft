package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.behavior.MutantConversion;
import com.asestefan.mutationcraft.effect.EffectProcedures;
import com.asestefan.mutationcraft.enchantment.MutationBane;
import com.asestefan.mutationcraft.entity.HumanStage1Entity;
import com.asestefan.mutationcraft.entity.HumanStage2Entity;
import com.asestefan.mutationcraft.entity.HumanStage3Entity;
import com.asestefan.mutationcraft.entity.NecroptorEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.item.TotemOfImmunityItem;
import com.asestefan.mutationcraft.network.ModVariables;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;

public class CommonEvents implements EventHandler {
    public CommonEvents() {
    }

    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker == null) {
            return;
        }
        if (attacker instanceof LivingEntity living && ModUtil.isMutant(living) && entity.getRandom().nextDouble() <= 0.4) {
            entity.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 6000, 0));
        }
        if (attacker instanceof Player player && player.getMainHandItem().isEmpty() && player.hasEffect(ModMobEffects.MUTAGEN_SICKNESS.ref())
                && entity.getRandom().nextDouble() <= 0.2) {
            entity.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 1200, 0));
        }
        bleedingAttack(entity, attacker);
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (entity.level().isClientSide()) {
            return amount;
        }
        return amount + MutationBane.bonusDamage(entity, source);
    }

    @Override
    public void onPlayerTick(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        TotemOfImmunityItem.tickImmunity(player);
        if (player.hasEffect(ModMobEffects.CORROSION.ref())) {
            EffectProcedures.corrodeArmor(player, player.getEffect(ModMobEffects.CORROSION.ref()).getAmplifier());
        }
    }

    @Override
    public void onLivingDeath(LivingEntity entity, DamageSource source) {
        if (!entity.hasEffect(ModMobEffects.MUTAGEN_SICKNESS.ref())) {
            return;
        }
        Supplier<? extends EntityType<? extends Mob>> result = mutagenResult(entity, source.getEntity());
        if (result != null) {
            MutantConversion.convert(entity, result.get(), SoundEvents.ZOMBIE_INFECT);
        }
    }

    @Override
    public void onEffectExpired(LivingEntity entity, MobEffectInstance instance) {
        //? if >=1.21 {
        /*boolean sickness = instance.getEffect().value() == ModMobEffects.MUTAGEN_SICKNESS.get();
        *///?} else {
        boolean sickness = instance.getEffect() == ModMobEffects.MUTAGEN_SICKNESS.get();
        //?}
        if (!sickness) {
            return;
        }
        Supplier<? extends EntityType<? extends Mob>> result = mutagenResult(entity, null);
        if (result != null) {
            MutationcraftMod.queueServerWork(entity.level(), 1, () -> {
                if (entity.isAlive() && MutantConversion.convert(entity, result.get(), SoundEvents.ZOMBIE_INFECT) != null) {
                    entity.discard();
                }
            });
            return;
        }
        if (entity.getRandom().nextDouble() > 0.3) {
            return;
        }
        MutationcraftMod.queueServerWork(entity.level(), 1, () -> {
            if (entity.isAlive()) {
                entity.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 3600, 0));
            }
        });
    }

    private static void bleedingAttack(LivingEntity entity, Entity attacker) {
        int duration;
        if (attacker instanceof HumanStage1Entity) {
            duration = 200;
        } else if (attacker instanceof HumanStage2Entity) {
            duration = 240;
        } else if (attacker instanceof HumanStage3Entity) {
            duration = 280;
        } else {
            return;
        }
        entity.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING.ref(), duration, 0));
    }

    private static Supplier<? extends EntityType<? extends Mob>> mutagenResult(LivingEntity entity, Entity killer) {
        if (ModUtil.isMutant(entity) || entity instanceof Player) {
            return null;
        }
        if (entity instanceof Pig) {
            return ModEntities.ASSIMILATED_PIG;
        } else if (entity instanceof WanderingTrader) {
            return ModEntities.ASSIMILATED_WANDERING_TRADER;
        } else if (entity instanceof Pillager) {
            return ModEntities.ASSIMILATED_PILLAGER;
        } else if (entity instanceof Spider) {
            return ModEntities.ASSIMILATED_SPIDER;
        } else if (entity.getType() == EntityType.ZOMBIE) {
            if (!(killer instanceof NecroptorEntity)) {
                return ModEntities.ASSIMILATED_HUMAN;
            }
        } else if (entity instanceof Horse) {
            return ModEntities.ASSIMILATED_HORSE;
        } else if (entity instanceof Villager) {
            return ModEntities.ASSIMILATED_VILLAGER;
        } else if (entity instanceof Piglin) {
            return ModEntities.ASSIMILATED_PIGLIN;
        } else if (entity instanceof Sheep) {
            return ModEntities.ASSIMILATED_SHEEP;
        } else if (entity instanceof Cow) {
            return ModEntities.ASSIMILATED_COW;
        } else if (entity instanceof Witch) {
            return ModEntities.ASSIMILATED_WITCH;
        } else if (entity instanceof EnderMan) {
            if (ModVariables.stage(entity.level()) == 2) {
                return ModEntities.ASSIMILATED_ENDERMAN;
            }
        } else if (entity instanceof Wolf) {
            return ModEntities.ASSIMILATED_WOLF;
        } else if (entity instanceof Fox) {
            return ModEntities.ASSIMILATED_FOX;
        } else if (entity instanceof PolarBear) {
            return ModEntities.ASSIMILATED_BEAR;
        } else if (entity instanceof Evoker) {
            return ModEntities.ASSIMILATED_EVOKER;
        } else if (entity instanceof Donkey) {
            return ModEntities.ASSIMILATED_DONKEY;
        } else if (entity instanceof Creeper) {
            return ModEntities.ASSIMILATED_CREEPER;
        }
        return null;
    }
}
