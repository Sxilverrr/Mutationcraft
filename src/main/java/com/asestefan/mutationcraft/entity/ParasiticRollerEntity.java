package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import net.minecraft.sounds.SoundSource;
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

public class ParasiticRollerEntity extends MutantEntity {
    public ParasiticRollerEntity(EntityType<? extends ParasiticRollerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("walk") : loop("idle");
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
        return ModSounds.MUTANT_HUMAN_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.HUSK_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        parasiticRollerPush(this, player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 45.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.1)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1);
    }

    private static final String COOLDOWN_KEY = "mutationcraft:touch_cooldown";

    private static void parasiticRollerPush(ParasiticRollerEntity roller, Player player) {
        if (roller.level().isClientSide() || !roller.isAlive() || player.isCreative() || player.isSpectator()
                || roller.getRandom().nextDouble() > 0.15 || !ModUtil.tryCooldown(roller, COOLDOWN_KEY, 20)) {
            return;
        }
        ModUtil.launchAway(roller, player, 1.0, 0.2);
        if (roller.getRandom().nextDouble() <= 0.2) {
            roller.level().playSound(null, roller.getX(), roller.getY(), roller.getZ(), SoundEvents.RAVAGER_ROAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }
}
