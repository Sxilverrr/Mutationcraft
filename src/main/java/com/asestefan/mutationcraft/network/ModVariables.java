package com.asestefan.mutationcraft.network;

import com.asestefan.mutationcraft.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
//? if >=1.21 {
/*import net.minecraft.core.HolderLookup;
*///?}

public final class ModVariables {
    public static final int MAP = 0;

    public static void onPlayerLoggedIn(ServerPlayer player) {
        Services.PLATFORM.sendVariablesToPlayer(player, MAP, MapVariables.get(player.level()).write(new CompoundTag()));
    }

    public static void handleSync(int type, CompoundTag data) {
        if (type == MAP) {
            MapVariables.clientSide.read(data);
        }
    }

    public static void tickTime(Level level) {
        if (level.isClientSide() || ModVariables.paused(level)) {
            return;
        }
        MapVariables data = MapVariables.get(level);
        data.time += 0.5;
        data.setDirty();
    }

    public static double time(LevelAccessor world) {
        return MapVariables.get(world).time;
    }

    public static int stage(LevelAccessor world) {
        return MapVariables.get(world).stage;
    }

    public static boolean paused(LevelAccessor world) {
        return MapVariables.get(world).paused;
    }

    public static class MapVariables extends SavedData {
        public static final String DATA_NAME = "mutationcraft_mapvars";
        static MapVariables clientSide = new MapVariables();
        public double time = 0.0;
        public int stage = 0;
        public boolean paused = false;

        public static MapVariables load(CompoundTag tag) {
            MapVariables data = new MapVariables();
            data.read(tag);
            return data;
        }

        public void read(CompoundTag nbt) {
            this.time = nbt.getDouble("time");
            this.stage = nbt.getInt("stage");
            this.paused = nbt.getBoolean("paused");
        }

        public CompoundTag write(CompoundTag nbt) {
            nbt.putDouble("time", this.time);
            nbt.putInt("stage", this.stage);
            nbt.putBoolean("paused", this.paused);
            return nbt;
        }

        //? if >=1.21 {
        /*@Override
        public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
            return write(nbt);
        }
        *///?} else {
        @Override
        public CompoundTag save(CompoundTag nbt) {
            return write(nbt);
        }
        //?}

        public static MapVariables get(LevelAccessor world) {
            if (world instanceof ServerLevelAccessor serverLevelAcc) {
                ServerLevel overworld = serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD);
                //? if >=1.21 {
                /*return overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(MapVariables::new, (tag, provider) -> load(tag), null), DATA_NAME);
                *///?} else {
                return overworld.getDataStorage().computeIfAbsent(MapVariables::load, MapVariables::new, DATA_NAME);
                //?}
            }
            return clientSide;
        }
    }

    private ModVariables() {
    }
}
