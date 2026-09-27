package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.ModUtil;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class FlamethrowerFuel {
    public static final int CAPACITY = 2400;
    private static final String TANK = "Tank";
    private static Map<Item, Integer> values;

    private static Map<Item, Integer> values() {
        if (values == null) {
            values = Map.of(
                    Items.COAL, 67,
                    Items.CHARCOAL, 67,
                    Items.FIRE_CHARGE, 100,
                    Items.BLAZE_POWDER, 133,
                    Items.COAL_BLOCK, 600,
                    Items.LAVA_BUCKET, 400);
        }
        return values;
    }

    public static boolean isFuel(ItemStack stack) {
        return values().containsKey(stack.getItem());
    }

    public static int totalFuel(ItemStack flamethrower) {
        return ModUtil.itemData(flamethrower).getInt(TANK);
    }

    public static boolean hasFuel(ItemStack flamethrower) {
        return totalFuel(flamethrower) > 0;
    }

    public static ItemStack absorb(ItemStack flamethrower, ItemStack fuel) {
        int value = values().getOrDefault(fuel.getItem(), 0);
        if (fuel.isEmpty() || value <= 0) {
            return fuel;
        }
        int space = CAPACITY - totalFuel(flamethrower);
        int used = Math.min(fuel.getCount(), space / value);
        if (used <= 0) {
            return fuel;
        }
        int added = used * value;
        ModUtil.updateItemData(flamethrower, data -> data.putInt(TANK, data.getInt(TANK) + added));
        if (fuel.is(Items.LAVA_BUCKET)) {
            return new ItemStack(Items.BUCKET);
        }
        return used >= fuel.getCount() ? ItemStack.EMPTY : fuel.copyWithCount(fuel.getCount() - used);
    }

    public static boolean consume(ItemStack flamethrower, int ticks) {
        int tank = totalFuel(flamethrower);
        if (tank < ticks) {
            return false;
        }
        ModUtil.updateItemData(flamethrower, data -> {
            if (tank - ticks <= 0) {
                data.remove(TANK);
            } else {
                data.putInt(TANK, tank - ticks);
            }
        });
        return true;
    }

    private FlamethrowerFuel() {
    }
}
