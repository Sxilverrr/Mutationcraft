package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.MutantConversion;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.util.GeckoLibUtil;
//? if <1.21
import net.minecraft.world.entity.MobType;
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

public abstract class MutantEntity extends Monster implements AnimatedMutant {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ProcedureAnimation procedure = new ProcedureAnimation(this.entityData, ANIMATION);

    protected MutantEntity(EntityType<? extends MutantEntity> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.setNoAi(false);
    }

    protected abstract String defaultTexture();

    protected abstract RawAnimation movementAnimation(AnimationState<?> event);

    //? if >=1.21 {
    /*@Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION, ProcedureAnimation.UNDEFINED);
        builder.define(TEXTURE, this.defaultTexture());
    }
    *///?} else {
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, ProcedureAnimation.UNDEFINED);
        this.entityData.define(TEXTURE, this.defaultTexture());
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

    //? if <1.21 {
    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }
    //?}

    protected MeleeAttackGoal meleeGoal(double speed) {
        return meleeGoal(this, speed);
    }

    public static MeleeAttackGoal meleeGoal(PathfinderMob mob, double speed) {
        return new MeleeAttackGoal(mob, speed, false) {
            //? if >=1.21 {
            /*@Override
            protected boolean canPerformAttack(LivingEntity entity) {
                return this.isTimeToAttack() && this.mob.distanceToSqr(entity) <= reach(this.mob, entity) && this.mob.getSensing().hasLineOfSight(entity);
            }
            *///?} else {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return reach(this.mob, entity);
            }
            //?}
        };
    }

    public static double reach(PathfinderMob mob, LivingEntity target) {
        double width = mob.getBbWidth() * 2.0;
        return width * width + target.getBbWidth();
    }

    protected void addLeapGoal(float height) {
        this.goalSelector.addGoal(0, new LeapAtTargetGoal(this, height));
    }

    protected void addStandardTargets(int priority) {
        addStandardTargets(this, this.targetSelector, priority);
    }

    public static void addStandardTargets(Monster mob, GoalSelector selector, int priority) {
        Class<?>[] targets = {Player.class, HazmatLeaderEntity.class, HazmatGuardEntity.class, HazmatFlamethrowerEntity.class, HazmatHelicopterEntity.class,
                IronGolem.class, Villager.class, Pillager.class, HazmatMedicEntity.class, WanderingTrader.class, Zombie.class, Witch.class, EnderMan.class,
                ScientistEntity.class, Evoker.class};
        for (Class<?> target : targets) {
            selector.addGoal(priority++, target(mob, target));
        }
        addPreyTarget(mob, selector, priority);
    }

    public static void addPreyTarget(Mob mob, GoalSelector selector, int priority) {
        selector.addGoal(priority, new NearestAttackableTargetGoal<>(mob, LivingEntity.class, 10, true, false, MutantEntity::isPrey));
    }

    public static boolean isPrey(LivingEntity entity) {
        return entity.isAlive() && !(entity instanceof Player) && !ModUtil.isMutant(entity)
                && !(entity instanceof ArmorStand) && !(entity instanceof WaterAnimal) && !(entity instanceof AmbientCreature);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static NearestAttackableTargetGoal<?> target(Monster mob, Class<?> type) {
        return new NearestAttackableTargetGoal(mob, type, true, true);
    }

    protected boolean weakConversion() {
        return false;
    }

    @Override
    public void awardKillScore(Entity entity, int score, DamageSource source) {
        super.awardKillScore(entity, score, source);
        MutantConversion.onKill(this, entity, this.weakConversion());
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

    private PlayState movementPredicate(AnimationState<MutantEntity> event) {
        if (this.procedure.isPlaying() || !this.isAlive()) {
            return PlayState.STOP;
        }
        RawAnimation animation = this.movementAnimation(event);
        if (animation == null) {
            return PlayState.STOP;
        }
        event.getController().setAnimation(animation);
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

    protected static RawAnimation loop(String name) {
        return RawAnimation.begin().thenLoop(name);
    }

    protected static boolean moving(AnimationState<?> event) {
        return event.isMoving() || !(event.getLimbSwingAmount() > -0.15F) || !(event.getLimbSwingAmount() < 0.15F);
    }
}
