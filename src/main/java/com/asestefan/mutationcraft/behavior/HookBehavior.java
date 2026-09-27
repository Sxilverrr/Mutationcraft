package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.HookEntity;
import com.asestefan.mutationcraft.init.ModBlocks;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModSounds;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;

public final class HookBehavior {
    private static final String YAW_KEY = "mutationcraft:hook_yaw";
    private static final int GROW_RETRY_TICKS = 20;

    public static void onSpawn(HookEntity hook, ServerLevelAccessor level) {
        if (level instanceof ServerLevel && !hook.level().noCollision(hook)) {
            ModUtil.teleportNear(hook, hook.getX(), hook.getY(), hook.getZ());
        }
        ModUtil.data(hook).putInt(hook.growthKey(), 0);
        if (MutationcraftConfig.HOOKS_PLACE_PUTRID_BLOCKS.get()) {
            level.setBlock(hook.blockPosition().below(), ModBlocks.PUTRID_BLOCK.get().defaultBlockState(), 3);
        }
    }

    public static void tick(HookEntity hook) {
        if (!(hook.level() instanceof ServerLevel level) || !hook.isAlive()) {
            return;
        }
        if (hook.grownType() != null && MutationcraftConfig.HOOKS_EVOLVE.get() && grow(level, hook, hook.grownType())) {
            return;
        }
        if (MutationcraftConfig.HOOKS_SUMMON_MOBS.get()) {
            summon(level, hook);
        }
    }

    private static boolean grow(ServerLevel level, HookEntity hook, Supplier<? extends EntityType<? extends Mob>> grownType) {
        CompoundTag data = ModUtil.data(hook);
        int timer = data.getInt(hook.growthKey()) + 1;
        data.putInt(hook.growthKey(), timer);
        if (timer < hook.growthTicks() || (timer - hook.growthTicks()) % GROW_RETRY_TICKS != 0) {
            return false;
        }
        EntityType<? extends Mob> type = grownType.get();
        if (!level.noCollision(type.getDimensions().makeBoundingBox(hook.position()))) {
            Mob probe = type.create(level);
            if (probe == null) {
                return false;
            }
            probe.moveTo(hook.getX(), hook.getY(), hook.getZ(), hook.getYRot(), 0.0F);
            boolean found = ModUtil.teleportNear(probe, hook.getX(), hook.getY(), hook.getZ());
            probe.discard();
            if (!found) {
                return false;
            }
            hook.moveTo(probe.getX(), probe.getY(), probe.getZ(), hook.getYRot(), 0.0F);
        }
        return MutantConversion.evolve(hook, type, ModSounds.MUTANT_TRANSFORM.get()) != null;
    }

    private static void summon(ServerLevel level, HookEntity hook) {
        RandomSource random = level.getRandom();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        if (r1 <= 0.03) {
            if (r2 <= 0.03) {
                summonOne(level, hook, ModEntities.NECROPTOR.get());
            }
        } else if (r1 <= 0.04 && r2 <= 0.04) {
            summonOne(level, hook, ModEntities.NECROPTOR.get());
        }
        double r3 = random.nextDouble();
        double r4 = random.nextDouble();
        double chance = r3 <= 0.05 ? 0.05 : r3 <= 0.06 ? 0.06 : -1.0;
        if (chance > 0.0 && r4 <= chance) {
            for (int i = 0; i < random.nextInt(3); i++) {
                summonOne(level, hook, ModEntities.MITER.get());
            }
        }
    }

    private static void summonOne(ServerLevel level, HookEntity hook, EntityType<? extends Mob> type) {
        double z = hook.getZ() + 1.0 + level.getRandom().nextDouble() * 3.0;
        AberrationSpawns.spawnNear(level, type, hook.getX(), hook.getY(), z);
    }

    public static void lockRotation(HookEntity hook) {
        float yaw = hook.getYRot();
        if (!hook.level().isClientSide()) {
            CompoundTag data = ModUtil.data(hook);
            if (!data.contains(YAW_KEY)) {
                data.putFloat(YAW_KEY, yaw);
            }
            yaw = data.getFloat(YAW_KEY);
        }
        hook.setYRot(yaw);
        hook.yRotO = yaw;
        hook.setXRot(0.0F);
        hook.xRotO = 0.0F;
        hook.yBodyRot = yaw;
        hook.yBodyRotO = yaw;
        hook.yHeadRot = yaw;
        hook.yHeadRotO = yaw;
    }

    public static void onDeath(HookEntity hook, Entity killer) {
        if (killer == null) {
            return;
        }
        AberrationSpawns.deathSpawns(hook);
        AberrationSpawns.mutagenSickness(killer, 2000);
    }

    private HookBehavior() {
    }
}
