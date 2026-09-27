package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public abstract class HazmatMob extends PathfinderMob {
    protected HazmatMob(EntityType<? extends HazmatMob> type, Level level) {
        super(type, level);
        //? if <1.21
        this.setMaxUpStep(0.6F);
    }

    protected boolean formerMonster() {
        return false;
    }

    @Override
    public void aiStep() {
        if (this.formerMonster()) {
            this.updateSwingTime();
            if (this.getLightLevelDependentMagicValue() > 0.5F) {
                this.noActionTime += 2;
            }
        }
        super.aiStep();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return this.formerMonster() ? -level.getPathfindingCostFromLightLevels(pos) : super.getWalkTargetValue(pos, level);
    }

    @Override
    public SoundSource getSoundSource() {
        return this.formerMonster() ? SoundSource.HOSTILE : super.getSoundSource();
    }

    //? if <1.21 {
    @Override
    public double getMyRidingOffset() {
        return -0.35;
    }
    //?}

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModUtil.sound("entity.zombie_villager.step"), 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModUtil.sound("entity.generic.hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModUtil.sound("entity.generic.death");
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return ModUtil.isHazmat(entity) || super.isAlliedTo(entity);
    }
}
