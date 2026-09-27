package com.asestefan.mutationcraft.item;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//? if <1.21 {
import net.minecraft.world.level.Level;
//?}

public class LoreItem extends Item {
    private final String lore;

    public LoreItem(Properties properties, String lore) {
        super(properties);
        this.lore = lore;
    }

    //? if >=1.21 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, list, flag);
        list.add(Component.literal(lore));
    }
    *///?} else {
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.literal(lore));
    }
    //?}
}
