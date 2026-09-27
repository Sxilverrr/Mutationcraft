package com.asestefan.mutationcraft.block;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModBlocks;
import com.asestefan.mutationcraft.init.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PutridBlock extends Block {
    public PutridBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState above = level.getBlockState(pos.above());
        if (above.is(Blocks.FIRE) || above.is(Blocks.SOUL_FIRE)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        if (MutationcraftConfig.PUTRID_BLOCK_SPREADING.get() && random.nextDouble() < 0.3) {
            spread(level, pos, random);
        }
    }

    private static void spread(ServerLevel level, BlockPos pos, RandomSource random) {
        if (canSpreadInto(level, pos.above(), pos.above())) {
            grow(level, pos.above(), pos.above(2), random);
        } else if (canSpreadInto(level, pos.below(), pos.below())) {
            grow(level, pos.below(), pos, random);
        } else if (canSpreadInto(level, pos.east(), pos.east())) {
            grow(level, pos.east(), pos.above(), random);
        } else if (canSpreadInto(level, pos.west(), pos.east())) {
            grow(level, pos.west(), pos.above(), random);
        } else if (canSpreadInto(level, pos.south(), pos.south())) {
            grow(level, pos.south(), pos.above(), random);
        } else if (canSpreadInto(level, pos.north(), pos.south())) {
            grow(level, pos.north(), pos.above(), random);
        } else {
            grow(level, pos, pos.above(), random);
        }
    }

    private static boolean canSpreadInto(ServerLevel level, BlockPos target, BlockPos vineCheck) {
        BlockState state = level.getBlockState(target);
        return !state.is(ModBlocks.PUTRID_BLOCK.get()) && !level.getBlockState(vineCheck).is(ModBlocks.PUTRID_VINE.get())
                && !state.is(Blocks.CAVE_AIR) && !state.is(Blocks.AIR);
    }

    private static void grow(ServerLevel level, BlockPos target, BlockPos vine, RandomSource random) {
        level.setBlock(target, ModBlocks.PUTRID_BLOCK.get().defaultBlockState(), 3);
        if (random.nextDouble() <= 0.15) {
            level.setBlock(vine, ModBlocks.PUTRID_VINE.get().defaultBlockState(), 3);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (level.isClientSide()) {
            return;
        }
        double r1 = level.getRandom().nextDouble();
        double r2 = level.getRandom().nextDouble();
        if (r1 <= 0.01) {
            if (r2 <= 0.1) {
                infect(entity, 4.0F, 1000);
            }
        } else if (r1 <= 0.02 && r2 <= 0.1) {
            infect(entity, 3.0F, 1000);
        }
    }

    public static void infect(Entity entity, float damage, int sicknessDuration) {
        if (entity instanceof LivingEntity living && ModUtil.isMutant(living)) {
            living.addEffect(new MobEffectInstance(ModMobEffects.RAGE.ref(), 300, 0));
            return;
        }
        entity.hurt(entity.damageSources().generic(), damage);
        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), sicknessDuration, 0));
        }
    }
}
