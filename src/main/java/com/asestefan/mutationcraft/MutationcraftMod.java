package com.asestefan.mutationcraft;

import com.asestefan.mutationcraft.init.ModBlocks;
import com.asestefan.mutationcraft.init.ModEnchantments;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModItems;
import com.asestefan.mutationcraft.init.ModMenus;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.init.ModParticles;
import com.asestefan.mutationcraft.init.ModPotions;
import com.asestefan.mutationcraft.init.ModSounds;
import com.asestefan.mutationcraft.init.ModTabs;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.world.level.LevelAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MutationcraftMod {
    public static final String MODID = "mutationcraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    private static final Collection<SimpleEntry<Runnable, Integer>> WORK_QUEUE = new ConcurrentLinkedQueue<>();

    public static void init() {
        ModSounds.REGISTRY.entries();
        ModBlocks.REGISTRY.entries();
        ModItems.REGISTRY.entries();
        ModEntities.REGISTRY.entries();
        ModMobEffects.REGISTRY.entries();
        ModPotions.REGISTRY.entries();
        ModEnchantments.init();
        ModParticles.REGISTRY.entries();
        ModTabs.REGISTRY.entries();
        ModMenus.REGISTRY.entries();
    }

    public static void queueServerWork(LevelAccessor world, int tick, Runnable action) {
        if (world.isClientSide()) {
            return;
        }
        WORK_QUEUE.add(new SimpleEntry<>(action, tick));
    }

    public static void onServerTick() {
        List<SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
        WORK_QUEUE.forEach(work -> {
            work.setValue(work.getValue() - 1);
            if (work.getValue() <= 0) {
                actions.add(work);
            }
        });
        actions.forEach(e -> e.getKey().run());
        WORK_QUEUE.removeAll(actions);
    }

    private MutationcraftMod() {
    }
}
