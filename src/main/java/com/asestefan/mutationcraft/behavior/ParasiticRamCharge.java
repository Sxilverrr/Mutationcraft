package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.ParasiticRamEntity;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public final class ParasiticRamCharge {
    private static final String COOLDOWN_KEY = "mutationcraft:touch_cooldown";

    public static void onTouch(ParasiticRamEntity ram, Player player) {
        if (!(ram.level() instanceof ServerLevel level) || !ram.isAlive() || player.isCreative() || player.isSpectator()
                || ram.getRandom().nextDouble() > 0.15 || !ModUtil.tryCooldown(ram, COOLDOWN_KEY, 20)) {
            return;
        }
        ram.playAnimation("ramming");
        double x = ram.getX();
        double y = ram.getY();
        double z = ram.getZ();
        MutationcraftMod.queueServerWork(level, 20, () -> {
            if (!player.isAlive() || player.level() != level) {
                return;
            }
            ModUtil.launchAway(ram, player, 1.2, 0.35);
            if (level.getRandom().nextDouble() <= 0.2) {
                level.playSound(null, x, y, z, SoundEvents.RAVAGER_ROAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        });
    }

    public static void onTarget(ParasiticRamEntity ram) {
        if (ram.getRandom().nextDouble() <= 0.05) {
            ram.addEffect(new MobEffectInstance(ModMobEffects.RAGE.ref(), 160, 1));
        }
    }

    private ParasiticRamCharge() {
    }
}
