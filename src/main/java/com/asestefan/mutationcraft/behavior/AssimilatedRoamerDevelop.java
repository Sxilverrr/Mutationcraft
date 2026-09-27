package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

public final class AssimilatedRoamerDevelop {
    private static final String KEY = "Develop";

    public static void tick(Mob roamer) {
        if (roamer.level().isClientSide() || !roamer.isAlive() || !MutationcraftConfig.ROAMER_DEVELOPS.get()) {
            return;
        }
        CompoundTag data = ModUtil.data(roamer);
        int ticks = data.getInt(KEY) + 1;
        if (ticks < MutationcraftConfig.ROAMER_DEVELOP_SECONDS.ticks()) {
            data.putInt(KEY, ticks);
            return;
        }
        data.remove(KEY);
        Component name = roamer.getCustomName();
        Mob developed = develop(roamer);
        if (developed != null && name != null) {
            developed.setCustomName(name);
        }
    }

    public static boolean developsFrom(Mob roamer, Entity victim) {
        return (victim instanceof Player || victim instanceof Villager) && MutationcraftConfig.ROAMER_DEVELOPS.get();
    }

    public static void fromKill(Mob roamer, Entity victim) {
        if (roamer.level().isClientSide() || !roamer.isAlive()) {
            return;
        }
        roamer.setCustomName(null);
        Mob developed = develop(roamer);
        if (developed != null && victim instanceof Player player) {
            developed.setCustomName(Component.literal(player.getDisplayName().getString()));
        }
    }

    private static Mob develop(Mob roamer) {
        return MutantConversion.evolve(roamer, ModEntities.DEVELOPED_ROAMER.get(), ModSounds.MUTANT_TRANSFORM.get());
    }

    private AssimilatedRoamerDevelop() {
    }
}
