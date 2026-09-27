package com.asestefan.mutationcraft.menu;

import com.asestefan.mutationcraft.init.ModMenus;
import com.asestefan.mutationcraft.item.FlamethrowerFuel;
import com.asestefan.mutationcraft.item.FlamethrowerItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FlamethrowerMenu extends AbstractContainerMenu {
    public static final int FUEL_X = 80;
    public static final int FUEL_Y = 35;
    private final SimpleContainer fuel = new SimpleContainer(1);
    private final Player player;
    private final InteractionHand hand;
    private final int lockedSlot;

    public static FlamethrowerMenu client(int id, Inventory inventory) {
        return new FlamethrowerMenu(id, inventory, inventory.getSelected().getItem() instanceof FlamethrowerItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    public FlamethrowerMenu(int id, Inventory inventory, InteractionHand hand) {
        super(ModMenus.FLAMETHROWER.get(), id);
        this.player = inventory.player;
        this.hand = hand;
        this.lockedSlot = hand == InteractionHand.MAIN_HAND ? inventory.selected : -1;
        if (!this.player.level().isClientSide()) {
            this.fuel.addListener(container -> this.absorb());
        }
        this.addSlot(new Slot(this.fuel, 0, FUEL_X, FUEL_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return FlamethrowerFuel.isFuel(stack);
            }
        });
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            if (column == this.lockedSlot) {
                this.addSlot(new Slot(inventory, column, 8 + column * 18, 142) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            } else {
                this.addSlot(new Slot(inventory, column, 8 + column * 18, 142));
            }
        }
    }

    public ItemStack flamethrower() {
        return this.player.getItemInHand(this.hand);
    }

    private void absorb() {
        ItemStack stack = this.flamethrower();
        ItemStack current = this.fuel.getItem(0);
        if (!(stack.getItem() instanceof FlamethrowerItem) || current.isEmpty()) {
            return;
        }
        ItemStack rest = FlamethrowerFuel.absorb(stack, current);
        if (!ItemStack.matches(rest, current)) {
            this.fuel.setItem(0, rest);
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (clickType == ClickType.SWAP && (button == this.lockedSlot || button == 40 && this.hand == InteractionHand.OFF_HAND)) {
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index == 0) {
            if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!FlamethrowerFuel.isFuel(stack) || !this.moveItemStackTo(stack, 0, 1, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.flamethrower().getItem() instanceof FlamethrowerItem;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            this.clearContainer(player, this.fuel);
        }
    }
}
