package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.init.ModSounds;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class AssimilatedEnderPearlItem extends Item {
    public AssimilatedEnderPearlItem() {
        super(new Properties().stacksTo(64).rarity(Rarity.COMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }
        BlockPos respawn = serverPlayer.getRespawnPosition();
        BlockPos target = respawn != null && serverPlayer.getRespawnDimension().equals(level.dimension()) ? respawn : level.getSharedSpawnPos();
        BlockPos from = serverPlayer.blockPosition();
        if (ModUtil.teleportNear(serverPlayer, target.getX() + 0.5, target.getY(), target.getZ() + 0.5)) {
            level.playSound(null, from, ModSounds.ASSIMILATED_ENDERMAN_TELEPORT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            if (!serverPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    //? if >=1.21 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, list, flag);
        list.add(Component.translatable("item.mutationcraft.assimilated_ender_pearl.hint").withStyle(ChatFormatting.GRAY));
    }
    *///?} else {
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("item.mutationcraft.assimilated_ender_pearl.hint").withStyle(ChatFormatting.GRAY));
    }
    //?}
}
