package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.init.ModSounds;
import com.asestefan.mutationcraft.network.ModVariables;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

public final class MutantConversion {
    public static final double ENDERMAN_TIME = 72000.0;
    private static final String CONVERTED_KEY = "mutationcraft:converted";
    private static Map<EntityType<?>, Supplier<? extends EntityType<? extends Mob>>> table;

    private static Map<EntityType<?>, Supplier<? extends EntityType<? extends Mob>>> table() {
        if (table == null) {
            Map<EntityType<?>, Supplier<? extends EntityType<? extends Mob>>> map = new HashMap<>();
            map.put(EntityType.SPIDER, ModEntities.ASSIMILATED_SPIDER);
            map.put(EntityType.CAVE_SPIDER, ModEntities.ASSIMILATED_SPIDER);
            map.put(EntityType.PIGLIN, ModEntities.ASSIMILATED_PIGLIN);
            map.put(EntityType.PIGLIN_BRUTE, ModEntities.ASSIMILATED_PIGLIN);
            map.put(EntityType.VILLAGER, ModEntities.ASSIMILATED_VILLAGER);
            map.put(EntityType.ZOMBIE_VILLAGER, ModEntities.ASSIMILATED_VILLAGER);
            map.put(EntityType.HORSE, ModEntities.ASSIMILATED_HORSE);
            map.put(EntityType.PIG, ModEntities.ASSIMILATED_PIG);
            map.put(EntityType.PILLAGER, ModEntities.ASSIMILATED_PILLAGER);
            map.put(EntityType.WANDERING_TRADER, ModEntities.ASSIMILATED_WANDERING_TRADER);
            map.put(EntityType.ZOMBIE, ModEntities.ASSIMILATED_HUMAN);
            map.put(EntityType.SHEEP, ModEntities.ASSIMILATED_SHEEP);
            map.put(EntityType.COW, ModEntities.ASSIMILATED_COW);
            map.put(EntityType.WITCH, ModEntities.ASSIMILATED_WITCH);
            map.put(EntityType.ENDERMAN, ModEntities.ASSIMILATED_ENDERMAN);
            map.put(EntityType.WOLF, ModEntities.ASSIMILATED_WOLF);
            map.put(EntityType.FOX, ModEntities.ASSIMILATED_FOX);
            map.put(EntityType.POLAR_BEAR, ModEntities.ASSIMILATED_BEAR);
            map.put(EntityType.EVOKER, ModEntities.ASSIMILATED_EVOKER);
            map.put(EntityType.DONKEY, ModEntities.ASSIMILATED_DONKEY);
            table = map;
        }
        return table;
    }

    public static void onKill(LivingEntity killer, Entity victim, boolean weak) {
        if (!(killer.level() instanceof ServerLevel level) || victim == null) {
            return;
        }
        if (victim instanceof Player player) {
            if (MutationcraftConfig.PLAYER_BECOMES_ROAMER.get() && player.hasEffect(ModMobEffects.MUTAGEN_SICKNESS.ref())) {
                playerToRoamer(player);
            }
            return;
        }
        if (weak && level.getRandom().nextDouble() > 0.1) {
            return;
        }
        Supplier<? extends EntityType<? extends Mob>> result = table().get(victim.getType());
        if (result == null) {
            return;
        }
        if (weak && victim.getType() == EntityType.DONKEY) {
            return;
        }
        if (!weak && victim.getType() == EntityType.ENDERMAN && ModVariables.time(level) < ENDERMAN_TIME) {
            return;
        }
        convert(victim, result.get(), weak ? ModSounds.MUTANT_TRANSFORM.get() : SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
    }

    public static Mob playerToRoamer(Player player) {
        if (!(player.level() instanceof ServerLevel level) || wasConverted(player)) {
            return null;
        }
        Mob roamer = convert(player, ModEntities.ASSIMILATED_ROAMER.get(), ModSounds.MUTANT_TRANSFORM.get());
        if (roamer != null) {
            roamer.setCustomName(Component.literal(player.getDisplayName().getString()));
        }
        return roamer;
    }

    public static Mob convert(Entity victim, EntityType<? extends Mob> type, SoundEvent sound) {
        if (!(victim.level() instanceof ServerLevel level) || wasConverted(victim)) {
            return null;
        }
        Mob mob = type.create(level);
        if (mob == null) {
            return null;
        }
        ModUtil.data(victim).putBoolean(CONVERTED_KEY, true);
        mob.moveTo(victim.getX(), victim.getY(), victim.getZ(), victim.getYRot(), victim.getXRot());
        mob.setYHeadRot(victim.getYHeadRot());
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.CONVERSION);
        if (!(victim instanceof Player)) {
            carryOver(victim, mob);
        }
        level.addFreshEntity(mob);
        effects(level, victim.getX(), victim.getY(), victim.getZ(), sound);
        return mob;
    }

    public static Mob evolve(Mob from, EntityType<? extends Mob> type, SoundEvent sound) {
        if (!(from.level() instanceof ServerLevel level) || !from.isAlive()) {
            return null;
        }
        Mob mob = from.convertTo(type, false);
        if (mob == null) {
            return null;
        }
        ModUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.CONVERSION);
        if (sound != null) {
            effects(level, mob.getX(), mob.getY(), mob.getZ(), sound);
        }
        return mob;
    }

    public static boolean wasConverted(Entity entity) {
        return ModUtil.data(entity).getBoolean(CONVERTED_KEY);
    }

    public static void effects(ServerLevel level, double x, double y, double z, SoundEvent sound) {
        if (sound != null) {
            level.playSound(null, x, y, z, sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
        level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 1.0, 3.0, 1.0, 1.0);
    }

    private static void carryOver(Entity victim, Mob mob) {
        if (victim.hasCustomName()) {
            mob.setCustomName(victim.getCustomName());
            mob.setCustomNameVisible(victim.isCustomNameVisible());
        }
        if (victim instanceof Mob old) {
            if (old.isPersistenceRequired()) {
                mob.setPersistenceRequired();
            }
            if (old.isLeashed() && old.getLeashHolder() != null) {
                mob.setLeashedTo(old.getLeashHolder(), true);
            }
            old.dropLeash(false, false);
        }
    }

    public static CompoundTag data(Entity entity) {
        return ModUtil.data(entity);
    }

    private MutantConversion() {
    }
}
