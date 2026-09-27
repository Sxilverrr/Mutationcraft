package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.AssimilatedRoamerSpawn;
import com.asestefan.mutationcraft.behavior.HumanoidDeath;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

public class DevelopedRoamerEntity extends MutantEntity {
    public DevelopedRoamerEntity(EntityType<? extends DevelopedRoamerEntity> type, Level level) {
        super(type, level);
        this.xpReward = 8;
        if (this.getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.addBasicGoals();
        this.goalSelector.addGoal(6, new BreakDoorGoal(this, difficulty -> true));
        this.addStandardTargets(7);
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("walk") : loop("idle");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.MUTANT_HUMAN_AMBIENT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        HumanoidDeath.execute(this, source, HumanoidDeath.SICKNESS);
    }

    //? if >=1.21 {
    /*@Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        AssimilatedRoamerSpawn.randomName(this);
        return result;
    }
    *///?} else {
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        AssimilatedRoamerSpawn.randomName(this);
        return result;
    }
    //?}

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.ARMOR, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }
}
