package com.asestefan.mutationcraft.behavior;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;

public final class AssimilatedRoamerSpawn {
    private static final String[] COMMON_NAMES = {"Flames", "DAKOTA", "Henry", "NPC_D", "TqLxQuanZ", "Kristofer"};
    private static final String[] RARE_NAMES = {"OnMod", "PepePlays", "NightfoxyMC", "Kara", "JC", "Eiki"};
    private static final double[] THRESHOLDS = {0.5, 0.6, 0.7, 0.8, 0.9, 0.91};

    public static void randomName(Mob mob) {
        RandomSource random = mob.getRandom();
        double first = random.nextDouble();
        double second = random.nextDouble();
        String[] names;
        if (first <= 0.98) {
            names = COMMON_NAMES;
        } else if (first <= 0.99) {
            names = RARE_NAMES;
        } else {
            return;
        }
        for (int i = 0; i < THRESHOLDS.length; i++) {
            if (second <= THRESHOLDS[i]) {
                mob.setCustomName(Component.literal(names[i]));
                return;
            }
        }
        if (names == COMMON_NAMES) {
            mob.setCustomName(Component.literal("Sxilverr"));
        }
    }

    private AssimilatedRoamerSpawn() {
    }
}
