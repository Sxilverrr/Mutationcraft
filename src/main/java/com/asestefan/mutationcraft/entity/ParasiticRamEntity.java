package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.ParasiticRamCharge;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class ParasiticRamEntity extends MutantEntity {
    public ParasiticRamEntity(EntityType<? extends ParasiticRamEntity> type, Level level) {
        super(type, level);
        this.xpReward = 8;
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("Walk") : loop("idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.addStandardTargets(6);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        ParasiticRamCharge.onTouch(this, player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2);
    }
}
