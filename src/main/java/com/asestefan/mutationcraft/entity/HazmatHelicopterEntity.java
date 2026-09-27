package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.HazmatHelicopterDrops;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.util.GeckoLibUtil;
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

public class HazmatHelicopterEntity extends PathfinderMob implements AnimatedMutant {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(HazmatHelicopterEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(HazmatHelicopterEntity.class, EntityDataSerializers.STRING);
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("fly");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ProcedureAnimation procedure = new ProcedureAnimation(this.entityData, ANIMATION);
    private int lifetimeTicks;
    private int drops;

    public HazmatHelicopterEntity(EntityType<? extends HazmatHelicopterEntity> type, Level level) {
        super(type, level);
        this.xpReward = 2;
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    //? if >=1.21 {
    /*@Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION, ProcedureAnimation.UNDEFINED);
        builder.define(TEXTURE, "hazmat_helicopter");
    }
    *///?} else {
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, ProcedureAnimation.UNDEFINED);
        this.entityData.define(TEXTURE, "hazmat_helicopter");
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
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FollowMobGoal(this, 1.0, 10.0F, 5.0F));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 20) {
            @Override
            protected Vec3 getPosition() {
                return HazmatHelicopterEntity.this.wanderPosition();
            }
        });
    }

    private Vec3 wanderPosition() {
        RandomSource random = this.getRandom();
        double x = this.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double z = this.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double y = this.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        int ground = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor(x), Mth.floor(z));
        return new Vec3(x, Mth.clamp(y, ground + 8.0, ground + 24.0), z);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.isAlive()) {
            this.lifetimeTicks++;
            if (this.lifetimeTicks >= MutationcraftConfig.HELICOPTER_LIFETIME_SECONDS.ticks()) {
                this.discard();
                return;
            }
            HazmatHelicopterDrops.tick(this);
        }
    }

    public boolean canDrop() {
        return this.drops < MutationcraftConfig.HELICOPTER_MAX_DROPS.getInt();
    }

    public void countDrop() {
        this.drops++;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("HelicopterAge", this.lifetimeTicks);
        tag.putInt("HazmatDrops", this.drops);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifetimeTicks = tag.getInt("HelicopterAge");
        this.drops = tag.getInt("HazmatDrops");
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HAZMAT_HELICOPTER_AMBIENT.get();
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
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
        this.setNoGravity(true);
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 25.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.FLYING_SPEED, 0.3);
    }

    private PlayState movementPredicate(AnimationState<HazmatHelicopterEntity> event) {
        if (this.procedure.isPlaying()) {
            return PlayState.STOP;
        }
        event.getController().setAnimation(FLY);
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
