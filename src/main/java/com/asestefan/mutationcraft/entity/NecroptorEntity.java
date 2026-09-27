package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.MutantConversion;
import com.asestefan.mutationcraft.behavior.NecroptorBehavior;
import com.asestefan.mutationcraft.init.ModSounds;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.util.GeckoLibUtil;
//? if <1.21 {
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobType;
//?}
//? if >=1.21 {
/*import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
//?}

public class NecroptorEntity extends Spider implements AnimatedMutant {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(NecroptorEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(NecroptorEntity.class, EntityDataSerializers.STRING);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ProcedureAnimation procedure = new ProcedureAnimation(this.entityData, ANIMATION);

    public NecroptorEntity(EntityType<? extends NecroptorEntity> type, Level level) {
        super(type, level);
        this.xpReward = 4;
        this.setNoAi(false);
    }

    //? if >=1.21 {
    /*@Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION, ProcedureAnimation.UNDEFINED);
        builder.define(TEXTURE, "necroptor");
    }
    *///?} else {
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, ProcedureAnimation.UNDEFINED);
        this.entityData.define(TEXTURE, "necroptor");
    }
    //?}

    @Override
    public String getTexture() {
        return this.entityData.get(TEXTURE);
    }

    public void setTexture(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    @Override
    public ProcedureAnimation procedureAnimation() {
        return this.procedure;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, MutantEntity.meleeGoal(this, 1.2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(0, new LeapAtTargetGoal(this, 0.5F));
        Class<?>[] targets = {Player.class, HazmatLeaderEntity.class, HazmatGuardEntity.class, HazmatFlamethrowerEntity.class, HazmatHelicopterEntity.class,
                IronGolem.class, Villager.class, Pillager.class, HazmatMedicEntity.class, WanderingTrader.class, Skeleton.class, Zombie.class, Horse.class,
                Pig.class, Fox.class, Sheep.class, Cow.class, Witch.class, Wolf.class, EnderMan.class, ScientistEntity.class, Evoker.class, Donkey.class,
                PolarBear.class};
        int priority = 7;
        for (Class<?> target : targets) {
            this.targetSelector.addGoal(priority++, MutantEntity.target(this, target));
        }
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    //? if <1.21 {
    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }
    //?}

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.NECROPTOR_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.MUTANT_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.MUTANT_ANIMAL_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        NecroptorBehavior.onDeath(this, source.getEntity());
    }

    //? if >=1.21 {
    /*@Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        clearSpiderExtras(this);
        return result;
    }
    *///?} else {
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        clearSpiderExtras(this);
        return result;
    }
    //?}

    static void clearSpiderExtras(Spider spider) {
        List<Entity> riders = List.copyOf(spider.getPassengers());
        spider.ejectPassengers();
        riders.forEach(Entity::discard);
        spider.removeAllEffects();
    }

    @Override
    public void awardKillScore(Entity entity, int score, DamageSource source) {
        super.awardKillScore(entity, score, source);
        MutantConversion.onKill(this, entity, false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;
        if (this.deathTime >= 20 && !this.level().isClientSide() && !this.isRemoved()) {
            //? if >=1.21 {
            /*this.dropExperience(null);
            *///?} else {
            this.dropExperience();
            //?}
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return ModUtil.isMutant(entity) || super.isAlliedTo(entity);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2);
    }

    private PlayState movementPredicate(AnimationState<NecroptorEntity> event) {
        if (this.procedure.isPlaying() || !this.isAlive()) {
            return PlayState.STOP;
        }
        boolean moving = event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F);
        event.getController().setAnimation(moving ? WALK : IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
        controllers.add(new AnimationController<>(this, "procedure", 4, this.procedure::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
