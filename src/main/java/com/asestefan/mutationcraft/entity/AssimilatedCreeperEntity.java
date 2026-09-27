package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import net.minecraft.world.level.GameType;
import com.asestefan.mutationcraft.behavior.AnimalDeath;
import com.asestefan.mutationcraft.behavior.AnimalHurt;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
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

public class AssimilatedCreeperEntity extends MutantEntity {
    public AssimilatedCreeperEntity(EntityType<? extends AssimilatedCreeperEntity> type, Level level) {
        super(type, level);
        this.xpReward = 6;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.targetSelector.addGoal(6, target(this, Player.class));
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        if (moving(event) && !this.isAggressive() || this.isAggressive() && event.isMoving()) {
            return loop("walk");
        }
        return loop("idle");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ASSIMILATED_CREEPER_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount + AnimalHurt.fireAspectBonus(source, true));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        AnimalDeath.execute(this, source, false);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        assimilatedCreeperFuse(this, player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    private static final int FUSE = 20;
    private static final String COOLDOWN = "mutationcraft:creeper_fuse";

    private static void assimilatedCreeperFuse(AssimilatedCreeperEntity creeper, Player player) {
        Level level = creeper.level();
        if (level.isClientSide() || !creeper.isAlive() || creeper.getHealth() > 10.0F || !MutationcraftConfig.ASSIMILATED_CREEPER_EXPLODES.get()) {
            return;
        }
        if (!ModUtil.isGameMode(player, GameType.SURVIVAL) && !ModUtil.isGameMode(player, GameType.ADVENTURE)) {
            return;
        }
        if (!ModUtil.tryCooldown(creeper, COOLDOWN, FUSE + 20)) {
            return;
        }
        creeper.playAnimation("explosion");
        MutationcraftMod.queueServerWork(level, FUSE, () -> {
            if (creeper.isAlive() && !creeper.isRemoved()) {
                level.explode(null, creeper.getX(), creeper.getY(), creeper.getZ(), (float) MutationcraftConfig.ASSIMILATED_CREEPER_EXPLOSION_POWER.get(), Level.ExplosionInteraction.MOB);
            }
        });
    }
}
