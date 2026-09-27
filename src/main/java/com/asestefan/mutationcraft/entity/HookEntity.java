package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.behavior.HookBehavior;
import com.asestefan.mutationcraft.init.ModSounds;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
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

public abstract class HookEntity extends MutantEntity {
    protected HookEntity(EntityType<? extends HookEntity> type, Level level) {
        super(type, level);
    }

    public abstract Supplier<? extends EntityType<? extends Mob>> grownType();

    public abstract String growthKey();

    public abstract int growthTicks();

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return loop("idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        Class<?>[] targets = {Player.class, HazmatLeaderEntity.class, HazmatGuardEntity.class, HazmatFlamethrowerEntity.class, HazmatHelicopterEntity.class,
                IronGolem.class, Villager.class, Pillager.class, HazmatMedicEntity.class, WanderingTrader.class, Zombie.class, Witch.class, EnderMan.class,
                ScientistEntity.class, Evoker.class};
        int priority = 4;
        for (Class<?> target : targets) {
            this.targetSelector.addGoal(priority++, this.hookTarget(target));
        }
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private NearestAttackableTargetGoal<?> hookTarget(Class<?> type) {
        return new NearestAttackableTargetGoal(this, type, true, false);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.HOOK_AMBIENT.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.MUTANT_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        HookBehavior.onDeath(this, source.getEntity());
    }

    //? if >=1.21 {
    /*@Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        HookBehavior.onSpawn(this, level);
        return result;
    }
    *///?} else {
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        HookBehavior.onSpawn(this, level);
        return result;
    }
    //?}

    @Override
    public void baseTick() {
        super.baseTick();
        HookBehavior.tick(this);
    }

    @Override
    public void tick() {
        super.tick();
        HookBehavior.lockRotation(this);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }
}
