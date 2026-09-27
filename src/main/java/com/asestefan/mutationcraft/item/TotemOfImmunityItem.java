package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.client.ClientHooks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class TotemOfImmunityItem extends LoreItem {
    public static final String IMMUNITY_KEY = "mutationcraft:totem_immunity_until";
    private static final int DURATION = 400;

    public TotemOfImmunityItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON), "This magic item will make it's user immune to any source of damage for 20 seconds!");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            ClientHooks.displayItemActivation(stack);
        } else {
            player.setInvulnerable(true);
            ModUtil.data(player).putLong(IMMUNITY_KEY, level.getGameTime() + DURATION);
            level.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
        player.getCooldowns().addCooldown(this, 12000);
        return InteractionResultHolder.pass(stack);
    }

    public static void tickImmunity(Player player) {
        if (player.level().isClientSide() || !ModUtil.data(player).contains(IMMUNITY_KEY)) {
            return;
        }
        if (player.level().getGameTime() >= ModUtil.data(player).getLong(IMMUNITY_KEY)) {
            ModUtil.data(player).remove(IMMUNITY_KEY);
            player.setInvulnerable(false);
        }
    }
}
