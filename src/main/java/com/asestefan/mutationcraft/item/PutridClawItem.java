package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class PutridClawItem extends Item {
    public PutridClawItem() {
        //? if >=1.21 {
        /*super(new Properties().stacksTo(64).rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
        *///?} else {
        super(new Properties().stacksTo(64).rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build()));
        //?}
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            entity.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 100, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
        }
        return result;
    }
}
