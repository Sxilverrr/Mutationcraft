package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModSounds;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public final class VillageSirens {
    private static final int CHECK_INTERVAL = 40;
    private static final int VILLAGE_RADIUS = 48;
    private static final int MIN_DELAY = 400;
    private static final int MAX_DELAY = 600;
    private static final float VOLUME = 6.0F;
    private static final Map<ServerLevel, Map<Long, Long>> NEXT = new WeakHashMap<>();

    public static void tick(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level) || entity.tickCount % CHECK_INTERVAL != 0 || !ModUtil.isMutant(entity)) {
            return;
        }
        if (!MutationcraftConfig.VILLAGE_SIRENS.get() || !level.isVillage(entity.blockPosition())) {
            return;
        }
        Optional<BlockPos> bell = level.getPoiManager().findClosest(type -> type.is(PoiTypes.MEETING), entity.blockPosition(), VILLAGE_RADIUS, PoiManager.Occupancy.ANY);
        if (bell.isEmpty()) {
            return;
        }
        long time = level.getGameTime();
        Map<Long, Long> next = NEXT.computeIfAbsent(level, key -> new HashMap<>());
        if (next.size() > 256) {
            next.values().removeIf(at -> at < time);
        }
        BlockPos pos = bell.get();
        if (next.getOrDefault(pos.asLong(), 0L) > time) {
            return;
        }
        next.put(pos.asLong(), time + MIN_DELAY + level.getRandom().nextInt(MAX_DELAY - MIN_DELAY));
        SoundEvent sound = level.getRandom().nextFloat() < 0.6F ? ModSounds.VILLAGE_ALARM.get() : ModSounds.VILLAGE_AMBULANCE.get();
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundSource.AMBIENT, VOLUME, 1.0F);
    }

    private VillageSirens() {
    }
}
