package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.entity.AssimilatedCreeperEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public final class AssimilatedCreeperFuse {
    private static final int FUSE = 20;
    private static final String COOLDOWN = "mutationcraft:creeper_fuse";

    public static void onPlayerTouch(AssimilatedCreeperEntity creeper, Player player) {
        Level level = creeper.level();
        if (level.isClientSide() || !creeper.isAlive() || creeper.getHealth() > 10.0F) {
            return;
        }
        if (!ModUtil.isGameMode(player, GameType.SURVIVAL) && !ModUtil.isGameMode(player, GameType.ADVENTURE)) {
            return;
        }
        if (!ModUtil.tryCooldown(creeper, COOLDOWN, FUSE + 20)) {
            return;
        }
        creeper.playAnimation("explosion");
        MutationcraftMod.queueServerWork(level, FUSE, () -> {
            if (creeper.isAlive() && !creeper.isRemoved()) {
                level.explode(null, creeper.getX(), creeper.getY(), creeper.getZ(), 5.0F, Level.ExplosionInteraction.MOB);
            }
        });
    }

    private AssimilatedCreeperFuse() {
    }
}
