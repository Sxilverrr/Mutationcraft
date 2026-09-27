package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.entity.PoisonedOrbEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class PoisonedOrbItem extends Item {
    public PoisonedOrbItem() {
        super(new Properties().durability(10000));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    //? if >=1.21 {
    /*@Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
    *///?} else {
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    //?}

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (level.isClientSide() || !(entity instanceof ServerPlayer player)) {
            return;
        }
        PoisonedOrbEntity.shoot(level, player, level.getRandom(), 1.2F, 5.0, 2);
        //? if >=1.21 {
        /*stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        *///?} else {
        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        //?}
        player.releaseUsingItem();
    }
}
