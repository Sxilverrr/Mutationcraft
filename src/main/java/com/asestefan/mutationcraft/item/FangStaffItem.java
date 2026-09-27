package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

public class FangStaffItem extends LoreItem {
    public FangStaffItem() {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC), "This legendary staff will summon evoker fangs from the ground to keep you safe!");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            summonFangs(serverLevel, player);
            player.addEffect(new MobEffectInstance(ModMobEffects.MAGIC_EXHAUSTION.ref(), 200, 0));
        }
        player.getCooldowns().addCooldown(this, 200);
        return InteractionResultHolder.pass(stack);
    }

    private static void summonFangs(ServerLevel level, Player player) {
        BlockPos hit = level.clip(new ClipContext(player.getEyePosition(1.0F), player.getEyePosition(1.0F).add(player.getViewVector(1.0F).scale(40.0)),
                ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player)).getBlockPos();
        double originX = player.getX();
        double originZ = player.getZ();
        double targetX = hit.getX() - originX + 0.5;
        double targetZ = hit.getZ() - originZ + 0.5;
        int total = (int) Math.round((Math.abs(targetX) + Math.abs(targetZ)) / 2.0);
        for (int i = 0; i < total; i++) {
            double grow = (double) i / total;
            MutationcraftMod.queueServerWork(level, i * 3, () -> {
                EvokerFangs fangs = EntityType.EVOKER_FANGS.create(level);
                if (fangs != null) {
                    fangs.moveTo(originX + targetX * grow, player.getY(), originZ + targetZ * grow, 0.0F, 0.0F);
                    level.addFreshEntity(fangs);
                }
            });
        }
    }
}
