package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.item.FlamethrowerFuel;
import com.asestefan.mutationcraft.item.FlamethrowerItem;
import com.asestefan.mutationcraft.menu.FlamethrowerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FlamethrowerScreen extends AbstractContainerScreen<FlamethrowerMenu> {
    private static final int PANEL = 0xFFC6C6C6;
    private static final int LIGHT = 0xFFFFFFFF;
    private static final int SHADOW = 0xFF555555;
    private static final int EDGE = 0xFF000000;
    private static final int SLOT = 0xFF8B8B8B;
    private static final int SLOT_SHADOW = 0xFF373737;
    private static final int GAUGE_BACK = 0xFF2B2B2B;
    private static final int FUEL_COLOR = 0xFFFF9A1F;
    private static final int HEAT_COLOR = 0xFFD8261C;
    private static final int OVERHEAT_COLOR = 0xFF7A7A7A;
    private static final int GAUGE_TOP = 17;
    private static final int GAUGE_HEIGHT = 52;
    private static final int FUEL_GAUGE_X = 56;
    private static final int HEAT_GAUGE_X = 114;
    private static final int GAUGE_WIDTH = 6;

    public FlamethrowerScreen(FlamethrowerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        //? if <1.21 {
        this.renderBackground(graphics);
        //?}
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
        ItemStack stack = this.menu.flamethrower();
        if (this.inside(mouseX, mouseY, FUEL_GAUGE_X)) {
            int seconds = FlamethrowerFuel.totalFuel(stack) / 20;
            graphics.renderTooltip(this.font, Component.translatable("item.mutationcraft.flamethrower.fuel", String.format("%d:%02d", seconds / 60, seconds % 60)), mouseX, mouseY);
        } else if (this.inside(mouseX, mouseY, HEAT_GAUGE_X)) {
            Component text = FlamethrowerItem.overheated(stack)
                    ? Component.translatable("item.mutationcraft.flamethrower.overheated")
                    : Component.translatable("item.mutationcraft.flamethrower.heat", FlamethrowerItem.heat(stack) * 100 / FlamethrowerItem.maxHeat());
            graphics.renderTooltip(this.font, text, mouseX, mouseY);
        }
    }

    private boolean inside(int mouseX, int mouseY, int gaugeX) {
        int x = this.leftPos + gaugeX;
        int y = this.topPos + GAUGE_TOP;
        return mouseX >= x - 1 && mouseX < x + GAUGE_WIDTH + 1 && mouseY >= y - 1 && mouseY < y + GAUGE_HEIGHT + 1;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        this.panel(graphics, x, y, this.imageWidth, this.imageHeight);
        for (Slot slot : this.menu.slots) {
            int sx = x + slot.x - 1;
            int sy = y + slot.y - 1;
            graphics.fill(sx, sy, sx + 18, sy + 18, SLOT_SHADOW);
            graphics.fill(sx + 1, sy + 1, sx + 18, sy + 18, LIGHT);
            graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, SLOT);
        }
        ItemStack stack = this.menu.flamethrower();
        float fuel = Math.min(1.0F, FlamethrowerFuel.totalFuel(stack) / (float) FlamethrowerFuel.CAPACITY);
        float heat = FlamethrowerItem.heat(stack) / (float) FlamethrowerItem.maxHeat();
        this.gauge(graphics, x + FUEL_GAUGE_X, y + GAUGE_TOP, fuel, FUEL_COLOR);
        this.gauge(graphics, x + HEAT_GAUGE_X, y + GAUGE_TOP, heat, FlamethrowerItem.overheated(stack) ? OVERHEAT_COLOR : HEAT_COLOR);
    }

    private void panel(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x + 2, y, x + width - 2, y + 1, EDGE);
        graphics.fill(x + 2, y + height - 1, x + width - 2, y + height, EDGE);
        graphics.fill(x, y + 2, x + 1, y + height - 2, EDGE);
        graphics.fill(x + width - 1, y + 2, x + width, y + height - 2, EDGE);
        graphics.fill(x + 1, y + 1, x + 2, y + 2, EDGE);
        graphics.fill(x + width - 2, y + 1, x + width - 1, y + 2, EDGE);
        graphics.fill(x + 1, y + height - 2, x + 2, y + height - 1, EDGE);
        graphics.fill(x + width - 2, y + height - 2, x + width - 1, y + height - 1, EDGE);
        graphics.fill(x + 2, y + 1, x + width - 2, y + height - 1, PANEL);
        graphics.fill(x + 1, y + 2, x + width - 1, y + height - 2, PANEL);
        graphics.fill(x + 2, y + 1, x + width - 3, y + 3, LIGHT);
        graphics.fill(x + 1, y + 2, x + 3, y + height - 3, LIGHT);
        graphics.fill(x + 3, y + height - 3, x + width - 2, y + height - 1, SHADOW);
        graphics.fill(x + width - 3, y + 3, x + width - 1, y + height - 2, SHADOW);
    }

    private void gauge(GuiGraphics graphics, int x, int y, float fill, int color) {
        graphics.fill(x - 1, y - 1, x + GAUGE_WIDTH + 1, y + GAUGE_HEIGHT + 1, SLOT_SHADOW);
        graphics.fill(x, y, x + GAUGE_WIDTH, y + GAUGE_HEIGHT, GAUGE_BACK);
        int height = Math.round(GAUGE_HEIGHT * fill);
        if (height > 0) {
            graphics.fill(x, y + GAUGE_HEIGHT - height, x + GAUGE_WIDTH, y + GAUGE_HEIGHT, color);
        }
    }
}
