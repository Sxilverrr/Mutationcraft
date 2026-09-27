package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.BloodSpikeEntity;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.asestefan.mutationcraft.entity.PoisonedSpikesEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CorrosionQueenSpells {
    private static final String COOLDOWN_KEY = "mutationcraft:corrosion_queen_spells";
    private static final int COOLDOWN = 100;
    private static final int[][] POISON_SPIKE_OFFSETS = {{4, 2}, {2, -2}, {-3, -2}, {-3, 3}, {-4, 4}, {1, 1}};

    public static void retaliate(CorrosionQueenEntity queen, Player player) {
        if (!(queen.level() instanceof ServerLevel level) || !ModUtil.tryCooldown(queen, COOLDOWN_KEY, COOLDOWN)) {
            return;
        }
        MutationcraftMod.queueServerWork(level, 60, () -> {
            if (!queen.isAlive() || level.getRandom().nextDouble() > 0.35) {
                return;
            }
            queen.playAnimation("fly");
            MutationcraftMod.queueServerWork(level, 20, () -> {
                if (queen.isAlive() && isValidTarget(player, level)) {
                    poisonSpikes(level, queen, player);
                }
            });
        });
        MutationcraftMod.queueServerWork(level, 80, () -> {
            if (!queen.isAlive()) {
                return;
            }
            if (level.getRandom().nextDouble() <= 0.6) {
                queen.playAnimation("spell2");
                MutationcraftMod.queueServerWork(level, 10, () -> {
                    if (queen.isAlive() && isValidTarget(player, level)) {
                        bloodSpike(level, queen, player);
                    }
                });
            }
            if (level.getRandom().nextDouble() <= 0.15) {
                queen.playAnimation("spell");
                MutationcraftMod.queueServerWork(level, 20, () -> {
                    if (queen.isAlive()) {
                        slowness(level, queen);
                    }
                });
            }
        });
    }

    private static boolean isValidTarget(Player player, ServerLevel level) {
        return player.isAlive() && player.level() == level;
    }

    private static void poisonSpikes(ServerLevel level, CorrosionQueenEntity queen, Player player) {
        for (int[] offset : POISON_SPIKE_OFFSETS) {
            PoisonedSpikesEntity spike = ModEntities.POISONED_SPIKES.get().create(level);
            if (spike == null) {
                continue;
            }
            double x = player.getX() + offset[0];
            double z = player.getZ() + offset[1];
            spike.moveTo(x, player.getY(), z, level.getRandom().nextFloat() * 360.0F, 0.0F);
            if (ModUtil.teleportNear(spike, x, player.getY(), z)) {
                ModUtil.finalizeSpawn(spike, level, level.getCurrentDifficultyAt(spike.blockPosition()), MobSpawnType.MOB_SUMMONED);
                level.addFreshEntity(spike);
            }
        }
        cast(level, queen, ModSounds.CORROSION_QUEEN_SUMMON_POISONED_SPIKES.get());
    }

    private static void bloodSpike(ServerLevel level, CorrosionQueenEntity queen, Player player) {
        playSound(level, queen, ModSounds.CORROSION_QUEEN_SUMMON_BLOOD_SPIKE.get());
        BloodSpikeEntity spike = ModEntities.BLOOD_SPIKE.get().create(level);
        if (spike != null) {
            float yaw = level.getRandom().nextFloat() * 360.0F;
            for (double height = 6.0; height >= 0.0; height -= 1.0) {
                spike.moveTo(player.getX(), player.getY() + height, player.getZ(), yaw, 0.0F);
                if (level.noCollision(spike)) {
                    ModUtil.finalizeSpawn(spike, level, level.getCurrentDifficultyAt(spike.blockPosition()), MobSpawnType.MOB_SUMMONED);
                    level.addFreshEntity(spike);
                    break;
                }
            }
        }
        particles(level, queen);
    }

    private static void slowness(ServerLevel level, CorrosionQueenEntity queen) {
        Vec3 center = queen.position();
        for (Player target : level.getEntitiesOfClass(Player.class, new AABB(center, center).inflate(16.0), p -> !p.isCreative() && !p.isSpectator())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 3));
            cast(level, queen, ModSounds.CORROSION_QUEEN_SPELL.get());
        }
    }

    private static void cast(ServerLevel level, CorrosionQueenEntity queen, SoundEvent sound) {
        playSound(level, queen, sound);
        particles(level, queen);
    }

    private static void playSound(ServerLevel level, CorrosionQueenEntity queen, SoundEvent sound) {
        level.playSound(null, queen.getX(), queen.getY(), queen.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private static void particles(ServerLevel level, CorrosionQueenEntity queen) {
        double x = queen.getX();
        double y = queen.getY();
        double z = queen.getZ();
        level.sendParticles(ModUtil.ambientEntityEffect(), x, y, z, 5, 0.5, 2.4, 0.7, 1.0);
        level.sendParticles(ModUtil.ambientEntityEffect(), x, y, z, 2, 0.6, 2.4, 0.7, 1.0);
        level.sendParticles(ModUtil.ambientEntityEffect(), x, y, z, 3, 0.5, 2.4, 0.8, 1.0);
        level.sendParticles(ModUtil.ambientEntityEffect(), x, y, z, 2, 0.6, 2.4, 0.8, 1.0);
    }

    private CorrosionQueenSpells() {
    }
}
