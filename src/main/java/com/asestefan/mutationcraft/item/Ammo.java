package com.asestefan.mutationcraft.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

final class Ammo {
    static ItemStack find(Player player, Item item) {
        if (player.getOffhandItem().is(item)) {
            return player.getOffhandItem();
        }
        if (player.getMainHandItem().is(item)) {
            return player.getMainHandItem();
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private Ammo() {
    }
}
