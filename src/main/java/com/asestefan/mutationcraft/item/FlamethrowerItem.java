package com.asestefan.mutationcraft.item;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.FlameSpray;
import com.asestefan.mutationcraft.client.FlamethrowerClient;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModItems;
import com.asestefan.mutationcraft.init.ModSounds;
import com.asestefan.mutationcraft.menu.FlamethrowerMenu;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
//? if >=1.21 {
/*import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.component.ItemAttributeModifiers;
*///?} else {
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
//?}

public class FlamethrowerItem extends Item {
    private static final int USE_DURATION = 72000;
    private static final int DURABILITY_INTERVAL = 5;
    private static final String HEAT = "Heat";
    private static final String OVERHEATED = "Overheated";

    public static int maxHeat() {
        return Math.max(1, MutationcraftConfig.FLAMETHROWER_OVERHEAT_SECONDS.ticks());
    }

    public FlamethrowerItem() {
        //? if >=1.21 {
        /*super(new Properties().durability(400).fireResistant().attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()));
        *///?} else {
        super(new Properties().durability(400).fireResistant());
        //?}
    }

    //? if <1.21 {
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND) {
            return super.getDefaultAttributeModifiers(slot);
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 3.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
    //?}

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repair.is(ModItems.SCRAP_METAL.get());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    //? if >=1.21 {
    /*@Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }
    *///?} else {
    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }
    //?}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isSecondaryUseActive() && FlamethrowerFuel.needed()) {
            if (!level.isClientSide()) {
                player.openMenu(new SimpleMenuProvider((id, inventory, owner) -> new FlamethrowerMenu(id, inventory, hand),
                        Component.translatable("container.mutationcraft.flamethrower")));
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        if (overheated(stack) || !player.getAbilities().instabuild && FlamethrowerFuel.needed() && !FlamethrowerFuel.hasFuel(stack)) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.6F, 1.4F);
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (!(level instanceof ServerLevel server)) {
            FlamethrowerClient.playLoop(entity, () -> entity.isUsingItem() && entity.getUseItem().getItem() instanceof FlamethrowerItem);
            return;
        }
        int used = USE_DURATION - remaining;
        boolean creative = entity instanceof Player player && player.getAbilities().instabuild;
        if (!creative) {
            if (FlamethrowerFuel.needed() && !FlamethrowerFuel.consume(stack, 1)) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.6F, 1.4F);
                entity.stopUsingItem();
                return;
            }
            if (MutationcraftConfig.FLAMETHROWER_OVERHEATS.get() && !FlameSpray.submerged(entity) && addHeat(stack)) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.FLAMETHROWER_OVERHEAT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                server.sendParticles(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getEyeY() - 0.4, entity.getZ(), 8, 0.2, 0.2, 0.2, 0.02);
                entity.stopUsingItem();
                return;
            }
            if (MutationcraftConfig.FLAMETHROWER_USES_DURABILITY.get() && used % DURABILITY_INTERVAL == 0) {
                //? if >=1.21 {
                /*stack.hurtAndBreak(1, entity, LivingEntity.getSlotForHand(entity.getUsedItemHand()));
                *///?} else {
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
                //?}
                if (stack.isEmpty()) {
                    entity.stopUsingItem();
                    return;
                }
            }
        }
        Vec3 direction = entity.getViewVector(1.0F);
        Vec3 origin = FlameSpray.tip(entity, direction);
        FlameSpray.particles(server, entity, origin, direction);
        if (used % 3 == 0) {
            FlameSpray.burn(server, entity, origin, direction, (float) MutationcraftConfig.FLAMETHROWER_DAMAGE.get(), stack);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide() || heat(stack) <= 0) {
            return;
        }
        if (entity instanceof LivingEntity living && living.isUsingItem() && living.getUseItem() == stack && !FlameSpray.submerged(living)) {
            return;
        }
        float heat = heat(stack) - maxHeat() / (float) Math.max(1, MutationcraftConfig.FLAMETHROWER_COOLDOWN_SECONDS.ticks());
        ModUtil.updateItemData(stack, data -> {
            if (heat <= 0.0F) {
                data.remove(HEAT);
                data.remove(OVERHEATED);
            } else {
                data.putFloat(HEAT, heat);
            }
        });
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!target.level().isClientSide() && target.getRandom().nextDouble() < MutationcraftConfig.FLAMETHROWER_MELEE_FIRE_CHANCE.get()) {
            ModUtil.setOnFire(target, 5);
        }
        return result;
    }

    //? if >=1.21 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, list, flag);
        tooltip(stack, list);
    }
    *///?} else {
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        tooltip(stack, list);
    }
    //?}

    private static void tooltip(ItemStack stack, List<Component> list) {
        if (FlamethrowerFuel.needed()) {
            int seconds = FlamethrowerFuel.totalFuel(stack) / 20;
            list.add(Component.translatable("item.mutationcraft.flamethrower.fuel", String.format("%d:%02d", seconds / 60, seconds % 60)).withStyle(ChatFormatting.GOLD));
        }
        if (overheated(stack)) {
            list.add(Component.translatable("item.mutationcraft.flamethrower.overheated").withStyle(ChatFormatting.RED));
        } else if (heat(stack) > 0) {
            list.add(Component.translatable("item.mutationcraft.flamethrower.heat", heatPercent(stack)).withStyle(ChatFormatting.RED));
        }
        if (FlamethrowerFuel.needed()) {
            list.add(Component.translatable("item.mutationcraft.flamethrower.hint").withStyle(ChatFormatting.GRAY));
        }
    }

    public static float heat(ItemStack stack) {
        return ModUtil.itemData(stack).getFloat(HEAT);
    }

    public static int heatPercent(ItemStack stack) {
        return Math.min(100, (int) Math.ceil(heat(stack) * 100.0F / maxHeat()));
    }

    public static boolean overheated(ItemStack stack) {
        return ModUtil.itemData(stack).getBoolean(OVERHEATED);
    }

    private static boolean addHeat(ItemStack stack) {
        float heat = Math.min(maxHeat(), heat(stack) + 1.0F);
        boolean overheat = heat >= maxHeat();
        ModUtil.updateItemData(stack, data -> {
            data.putFloat(HEAT, heat);
            if (overheat) {
                data.putBoolean(OVERHEATED, true);
            }
        });
        return overheat;
    }
}
