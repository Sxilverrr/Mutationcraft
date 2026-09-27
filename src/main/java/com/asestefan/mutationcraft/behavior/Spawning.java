package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.network.ModVariables;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

public final class Spawning {
    public static final double[] COMMON = {36000.0, 1.0, 1.0, 1.0, 1.0};
    public static final double[] PARASITE = {36000.0, 1.0, 1.0, 1.0};
    public static final double[] ADVANCED = {108000.0, 72000.0, 1.0, 1.0, 1.0};
    public static final double[] ELITE = {252000.0, 216000.0, 144000.0, 1.0, 1.0};
    public static final double[] BOSS = {540000.0, 504000.0, 432000.0, 288000.0, 1.0};

    public static boolean overworld(ServerLevelAccessor world, double[] thresholds) {
        return staged(world, thresholds, Level.OVERWORLD);
    }

    public static boolean nether(ServerLevelAccessor world, double[] thresholds) {
        return staged(world, thresholds, Level.NETHER);
    }

    public static boolean anywhere(ServerLevelAccessor world, double[] thresholds) {
        return staged(world, thresholds, null);
    }

    public static boolean always(ServerLevelAccessor world) {
        return MutationcraftConfig.MOBS_SPAWN_NATURALLY.get();
    }

    public static boolean hazmat(ServerLevelAccessor world, MobSpawnType reason) {
        return reason != MobSpawnType.CHUNK_GENERATION && always(world) && dimension(world) == Level.OVERWORLD;
    }

    public static boolean hazmatFlamethrower(ServerLevelAccessor world, MobSpawnType reason) {
        return hazmat(world, reason) && MutationcraftConfig.FLAMETHROWER_SPAWNS_NATURALLY.get();
    }

    public static boolean helicopter(ServerLevelAccessor world, MobSpawnType reason) {
        return hazmat(world, reason) && MutationcraftConfig.HELICOPTER_SPAWNS_NATURALLY.get();
    }

    private static boolean staged(ServerLevelAccessor world, double[] thresholds, ResourceKey<Level> dimension) {
        if (!always(world)) {
            return false;
        }
        int stage = ModVariables.stage(world);
        if (stage < 0 || stage >= thresholds.length) {
            return false;
        }
        return (dimension == null || dimension(world) == dimension) && ModVariables.time(world) > thresholds[stage];
    }

    private static ResourceKey<Level> dimension(LevelAccessor world) {
        return world instanceof Level level ? level.dimension() : world instanceof ServerLevelAccessor server ? server.getLevel().dimension() : Level.OVERWORLD;
    }

    private Spawning() {
    }
}
