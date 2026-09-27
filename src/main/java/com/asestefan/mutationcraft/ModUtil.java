package com.asestefan.mutationcraft;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.function.Consumer;
import com.asestefan.mutationcraft.entity.HazmatGuardEntity;
import com.asestefan.mutationcraft.entity.HazmatLeaderEntity;
import com.asestefan.mutationcraft.entity.HazmatFlamethrowerEntity;
import com.asestefan.mutationcraft.entity.HazmatMedicEntity;
import com.asestefan.mutationcraft.entity.ScientistEntity;
import com.asestefan.mutationcraft.entity.HazmatHelicopterEntity;
import com.asestefan.mutationcraft.client.ClientHooks;
import com.asestefan.mutationcraft.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
//? if >=1.21 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.ItemEnchantments;
*///?} else {
import java.util.function.Supplier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
//?}

public final class ModUtil {
    public static final TagKey<EntityType<?>> MUTANTS = TagKey.create(Registries.ENTITY_TYPE, id(MutationcraftMod.MODID, "mutants"));

    public static ResourceLocation id(String location) {
        //? if >=1.21 {
        /*return ResourceLocation.parse(location);
        *///?} else {
        return new ResourceLocation(location);
        //?}
    }

    public static ResourceLocation id(String namespace, String path) {
        //? if >=1.21 {
        /*return ResourceLocation.fromNamespaceAndPath(namespace, path);
        *///?} else {
        return new ResourceLocation(namespace, path);
        //?}
    }

    public static SoundEvent sound(String location) {
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(id(location));
        if (sound == null) {
            MutationcraftMod.LOGGER.warn("Unknown sound {}", location);
            return SoundEvents.EMPTY;
        }
        return sound;
    }

    public static boolean isHazmat(Entity entity) {
        return entity instanceof HazmatGuardEntity || entity instanceof HazmatLeaderEntity || entity instanceof HazmatFlamethrowerEntity
                || entity instanceof HazmatMedicEntity || entity instanceof ScientistEntity || entity instanceof HazmatHelicopterEntity;
    }

    public static boolean isMutant(Entity entity) {
        return entity.getType().is(MUTANTS);
    }

    public static boolean tryCooldown(Entity entity, String key, int ticks) {
        CompoundTag data = data(entity);
        long now = entity.level().getGameTime();
        if (now < data.getLong(key)) {
            return false;
        }
        data.putLong(key, now + ticks);
        return true;
    }

    public static void launchAway(Entity from, Entity target, double horizontal, double vertical) {
        double dx = target.getX() - from.getX();
        double dz = target.getZ() - from.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 1.0E-4) {
            dx = -Math.sin(Math.toRadians(from.getYRot()));
            dz = Math.cos(Math.toRadians(from.getYRot()));
            length = 1.0;
        }
        target.setDeltaMovement(dx / length * horizontal, vertical, dz / length * horizontal);
        target.hurtMarked = true;
    }

    public static CompoundTag itemData(ItemStack stack) {
        //? if >=1.21 {
        /*return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        *///?} else {
        return stack.getTag() == null ? new CompoundTag() : stack.getTag().copy();
        //?}
    }

    public static void updateItemData(ItemStack stack, Consumer<CompoundTag> updater) {
        //? if >=1.21 {
        /*CustomData.update(DataComponents.CUSTOM_DATA, stack, updater);
        *///?} else {
        updater.accept(stack.getOrCreateTag());
        //?}
    }

    public static CompoundTag data(Entity entity) {
        return Services.PLATFORM.getPersistentData(entity);
    }

    public static void finalizeSpawn(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType type) {
        //? if >=1.21 {
        /*mob.finalizeSpawn(level, difficulty, type, null);
        *///?} else {
        mob.finalizeSpawn(level, difficulty, type, null, null);
        //?}
    }

    public static boolean isGameMode(Entity entity, GameType mode) {
        if (entity instanceof ServerPlayer player) {
            return player.gameMode.getGameModeForPlayer() == mode;
        }
        if (entity instanceof Player player && player.level().isClientSide()) {
            return ClientHooks.gameMode(player) == mode;
        }
        return false;
    }

    public static boolean teleportNear(Entity entity, double x, double y, double z) {
        Level level = entity.level();
        RandomSource random = entity.level().getRandom();
        for (int attempt = 0; attempt < 16; attempt++) {
            double tx = attempt == 0 ? x : x + (random.nextDouble() - 0.5) * 6.0;
            double tz = attempt == 0 ? z : z + (random.nextDouble() - 0.5) * 6.0;
            BlockPos.MutableBlockPos pos = BlockPos.containing(tx, y + 2.0, tz).mutable();
            int minY = pos.getY() - 8;
            while (pos.getY() > minY && !level.getBlockState(pos.below()).blocksMotion()) {
                pos.move(Direction.DOWN);
            }
            if (!level.getBlockState(pos.below()).blocksMotion()) {
                continue;
            }
            double ty = pos.getY();
            AABB box = entity.getBoundingBox().move(tx - entity.getX(), ty - entity.getY(), tz - entity.getZ());
            if (level.noCollision(entity, box) && !level.containsAnyLiquid(box)) {
                entity.teleportTo(tx, ty, tz);
                if (entity instanceof Mob mob) {
                    mob.getNavigation().stop();
                }
                return true;
            }
        }
        return false;
    }

    public static void setOnFire(Entity entity, int seconds) {
        //? if >=1.21 {
        /*entity.igniteForSeconds(seconds);
        *///?} else {
        entity.setSecondsOnFire(seconds);
        //?}
    }

    public static ParticleOptions entityEffect() {
        //? if >=1.21 {
        /*return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xFFFFFFFF);
        *///?} else {
        return ParticleTypes.ENTITY_EFFECT;
        //?}
    }

    public static ParticleOptions ambientEntityEffect() {
        //? if >=1.21 {
        /*return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0x26FFFFFF);
        *///?} else {
        return ParticleTypes.AMBIENT_ENTITY_EFFECT;
        //?}
    }

    public static void damageEquipment(LivingEntity entity, EquipmentSlot slot, int amount) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return;
        }
        //? if >=1.21 {
        /*stack.hurtAndBreak(amount, entity, slot);
        *///?} else {
        stack.hurtAndBreak(amount, entity, e -> e.broadcastBreakEvent(slot));
        //?}
    }

    //? if >=1.21 {
    /*public static int enchantmentLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
        ItemEnchantments enchantments = stack.getEnchantments();
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            if (holder.is(enchantment)) {
                return enchantments.getLevel(holder);
            }
        }
        return 0;
    }
    *///?} else {
    public static int enchantmentLevel(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
    }

    public static int enchantmentLevel(Supplier<Enchantment> enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack);
    }
    //?}

    private ModUtil() {
    }
}
