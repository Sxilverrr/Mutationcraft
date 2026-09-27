package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.NecroptorEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;

public final class NecroptorBehavior {
    private static final String ZOMBIE_KILLS_KEY = "mutationcraft:necroptor_kills";

    public static void onDeath(NecroptorEntity necroptor, Entity killer) {
        if (killer != null) {
            AberrationSpawns.mutagenSickness(killer, 2000);
        }
    }

    public static void onAttack(LivingEntity victim, Entity attacker) {
        if (attacker instanceof NecroptorEntity && victim.getRandom().nextDouble() < MutationcraftConfig.NECROPTOR_SLOWNESS_CHANCE.get()) {
            victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
        }
    }

    public static void onAnyDeath(LivingEntity entity, Entity killer) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (entity instanceof NecroptorEntity) {
            spawnHook(level, entity);
        }
        if (killer instanceof NecroptorEntity necroptor && entity instanceof Zombie) {
            countZombieKill(level, necroptor);
        }
    }

    private static void spawnHook(ServerLevel level, LivingEntity necroptor) {
        if (!MutationcraftConfig.HOOKS_FROM_NECROPTORS.get() || level.getRandom().nextDouble() >= MutationcraftConfig.HOOK_FROM_NECROPTOR_CHANCE.get()) {
            return;
        }
        RandomSource random = level.getRandom();
        double x = necroptor.getX() + random.nextIntBetweenInclusive(-5, 5);
        double z = necroptor.getZ() + random.nextIntBetweenInclusive(-5, 5);
        AberrationSpawns.spawnNear(level, ModEntities.LIGHT_HOOK.get(), x, necroptor.getY(), z);
        level.playSound(null, necroptor.getX(), necroptor.getY(), necroptor.getZ(), SoundEvents.BELL_RESONATE, SoundSource.MASTER, 1.0F, 0.3F);
    }

    private static void countZombieKill(ServerLevel level, NecroptorEntity necroptor) {
        if (!MutationcraftConfig.NECROPTOR_EVOLVES.get()) {
            return;
        }
        CompoundTag data = ModUtil.data(necroptor);
        int kills = data.getInt(ZOMBIE_KILLS_KEY) + 1;
        data.putInt(ZOMBIE_KILLS_KEY, kills);
        if (kills >= MutationcraftConfig.NECROPTOR_EVOLVE_KILLS.getInt()) {
            MutationcraftMod.queueServerWork(level, 1, () -> MutantConversion.evolve(necroptor, ModEntities.REDUCTOR.get(), ModSounds.MUTANT_TRANSFORM.get()));
        }
    }

    private NecroptorBehavior() {
    }
}
