package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
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

public class HumanHerderEntity extends MutantEntity {
    public HumanHerderEntity(EntityType<? extends HumanHerderEntity> type, Level level) {
        super(type, level);
        this.xpReward = 14;
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("walk") : loop("Idle");
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
        return ModSounds.HUMAN_HERDER_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.HUMAN_HERDER_STEP.get(), 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        humanHerderSlam(this, player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 120.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 15.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2);
    }

    private static final String COOLDOWN_KEY = "mutationcraft:touch_cooldown";

    private static void humanHerderSlam(HumanHerderEntity herder, Player player) {
        if (!(herder.level() instanceof ServerLevel level) || !herder.isAlive() || player.isCreative() || player.isSpectator()
                || herder.getRandom().nextDouble() > 0.15 || !ModUtil.tryCooldown(herder, COOLDOWN_KEY, 40)) {
            return;
        }
        herder.playAnimation("slam");
        double x = herder.getX();
        double y = herder.getY();
        double z = herder.getZ();
        MutationcraftMod.queueServerWork(level, 30, () -> {
            if (!player.isAlive() || player.level() != level) {
                return;
            }
            ModUtil.launchAway(herder, player, 2.0, 1.2);
            level.playSound(null, x, y, z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        });
    }
}
