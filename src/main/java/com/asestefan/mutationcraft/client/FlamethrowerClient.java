package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.behavior.FlameSpray;
import com.asestefan.mutationcraft.init.ModSounds;
import com.asestefan.mutationcraft.item.FlamethrowerItem;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class FlamethrowerClient {
    private static final int OVERHEAT_COLOR = 0xFF2020;
    private static final int COOLDOWN_TICKS = 20;
    private static final Map<Entity, FlamethrowerLoopSound> LOOPS = new WeakHashMap<>();

    public static void playLoop(Entity source, BooleanSupplier active) {
        FlamethrowerLoopSound current = LOOPS.get(source);
        if (current != null && !current.isStopped() && !current.isFading()) {
            return;
        }
        boolean water = source instanceof LivingEntity living && FlameSpray.submerged(living);
        FlamethrowerLoopSound sound = new FlamethrowerLoopSound(water ? ModSounds.FLAMETHROWER_BUBBLE.get() : ModSounds.FLAMETHROWER_USE.get(),
                source instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, source,
                () -> active.getAsBoolean() && (source instanceof LivingEntity living && FlameSpray.submerged(living)) == water);
        LOOPS.put(source, sound);
        Minecraft.getInstance().getSoundManager().play(sound);
    }

    public static float heatLevel(ItemStack stack) {
        float heat = FlamethrowerItem.heat(stack);
        if (FlamethrowerItem.overheated(stack)) {
            return heat > COOLDOWN_TICKS ? 1.0F : 0.99F * heat / COOLDOWN_TICKS;
        }
        return Math.min(0.99F, heat / FlamethrowerItem.maxHeat());
    }

    public static HumanoidModel.ArmPose armPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
        if (entity.isUsingItem() && entity.getUsedItemHand() == hand && entity.getUseItem().getItem() instanceof FlamethrowerItem) {
            return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
        return null;
    }

    public static void renderOverlay(GuiGraphics graphics, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.options.hideGui) {
            return;
        }
        ItemStack stack = player.getMainHandItem().getItem() instanceof FlamethrowerItem ? player.getMainHandItem() : player.getOffhandItem();
        if (!(stack.getItem() instanceof FlamethrowerItem) || !FlamethrowerItem.overheated(stack)) {
            return;
        }
        int alpha = Math.round(255.0F * FlamethrowerItem.heat(stack) / FlamethrowerItem.maxHeat());
        if (alpha < 8) {
            return;
        }
        Component text = Component.translatable("hud.mutationcraft.overheat").withStyle(ChatFormatting.BOLD);
        graphics.drawString(minecraft.font, text, (width - minecraft.font.width(text)) / 2, height - 72, (alpha << 24) | OVERHEAT_COLOR, true);
    }

    private FlamethrowerClient() {
    }
}
