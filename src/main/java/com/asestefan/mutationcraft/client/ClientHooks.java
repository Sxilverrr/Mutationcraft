package com.asestefan.mutationcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public final class ClientHooks {
    public static GameType gameMode(Player player) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return null;
        }
        PlayerInfo info = connection.getPlayerInfo(player.getGameProfile().getId());
        return info == null ? null : info.getGameMode();
    }

    public static void displayItemActivation(ItemStack stack) {
        Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
    }

    private ClientHooks() {
    }
}
