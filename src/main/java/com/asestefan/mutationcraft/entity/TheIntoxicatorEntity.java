package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.TheIntoxicatorBehavior;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
//? if <1.21
import net.minecraft.nbt.CompoundTag;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class TheIntoxicatorEntity extends MutantEntity {
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);

    public TheIntoxicatorEntity(EntityType<? extends TheIntoxicatorEntity> type, Level level) {
        super(type, level);
        this.xpReward = 50;
        this.setPersistenceRequired();
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
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.THE_INTOXICATOR_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_ANIMAL_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DRAGON_BREATH)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!this.level().isClientSide()) {
            this.playAnimation("death");
        }
    }

    //? if >=1.21 {
    /*@Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        TheIntoxicatorBehavior.onSpawn(this);
        return result;
    }
    *///?} else {
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        TheIntoxicatorBehavior.onSpawn(this);
        return result;
    }
    //?}

    @Override
    public void baseTick() {
        super.baseTick();
        TheIntoxicatorBehavior.tick(this);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        TheIntoxicatorBehavior.onTouch(this, player);
    }

    //? if >=1.21 {
    /*@Override
    public boolean canChangeDimensions(Level from, Level to) {
        return false;
    }
    *///?} else {
    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    //?}

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;
        if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {
            //? if >=1.21 {
            /*this.dropExperience(null);
            *///?} else {
            this.dropExperience();
            //?}
            this.remove(RemovalReason.KILLED);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 350.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.1)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2);
    }
}
