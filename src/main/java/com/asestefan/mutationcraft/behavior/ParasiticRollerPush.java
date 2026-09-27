package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.entity.ParasiticRollerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public final class ParasiticRollerPush {
    private static final String COOLDOWN_KEY = "mutationcraft:touch_cooldown";

    public static void onTouch(ParasiticRollerEntity roller, Player player) {
        if (roller.level().isClientSide() || !roller.isAlive() || player.isCreative() || player.isSpectator()
                || roller.getRandom().nextDouble() > 0.15 || !ModUtil.tryCooldown(roller, COOLDOWN_KEY, 20)) {
            return;
        }
        ModUtil.launchAway(roller, player, 1.0, 0.2);
        if (roller.getRandom().nextDouble() <= 0.2) {
            roller.level().playSound(null, roller.getX(), roller.getY(), roller.getZ(), SoundEvents.RAVAGER_ROAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    private ParasiticRollerPush() {
    }
}
