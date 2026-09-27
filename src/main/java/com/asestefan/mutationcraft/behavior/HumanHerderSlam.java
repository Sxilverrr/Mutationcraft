package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.HumanHerderEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public final class HumanHerderSlam {
    private static final String COOLDOWN_KEY = "mutationcraft:touch_cooldown";

    public static void onTouch(HumanHerderEntity herder, Player player) {
        if (!(herder.level() instanceof ServerLevel level) || !herder.isAlive() || player.isCreative() || player.isSpectator()
                || herder.getRandom().nextDouble() > 0.15 || !ModUtil.tryCooldown(herder, COOLDOWN_KEY, 40)) {
            return;
        }
        herder.playAnimation("slam");
        double x = herder.getX();
        double y = herder.getY();
        double z = herder.getZ();
        MutationcraftMod.queueServerWork(level, 30, () -> {
            if (!player.isAlive() || player.level() != level) {
                return;
            }
            ModUtil.launchAway(herder, player, 2.0, 1.2);
            level.playSound(null, x, y, z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        });
    }

    private HumanHerderSlam() {
    }
}
