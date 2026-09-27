package com.asestefan.mutationcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PutridVineBlock extends SugarCaneBlock {
    public PutridVineBlock(Properties properties) {
        super(properties);
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 100;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(this) || below.isFaceSturdy(level, pos.below(), Direction.UP) || super.canSurvive(state, level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isEmptyBlock(pos.above())) {
            return;
        }
        int height = 1;
        while (level.getBlockState(pos.below(height)).is(this)) {
            height++;
        }
        if (height >= 7) {
            return;
        }
        int age = state.getValue(AGE);
        if (age == 15) {
            level.setBlockAndUpdate(pos.above(), defaultBlockState());
            level.setBlock(pos, state.setValue(AGE, 0), 4);
        } else {
            level.setBlock(pos, state.setValue(AGE, age + 1), 4);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (level.isClientSide()) {
            return;
        }
        double r1 = level.getRandom().nextDouble();
        double r2 = level.getRandom().nextDouble();
        if (r1 <= 0.01) {
            if (r2 <= 0.1) {
                PutridBlock.infect(entity, 2.0F, 2000);
            }
        } else if (r1 <= 0.02 && r2 <= 0.2) {
            PutridBlock.infect(entity, 1.0F, 2000);
        }
    }
}
