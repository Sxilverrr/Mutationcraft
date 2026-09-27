package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class CorrosionQueenEntity extends MutantEntity {
    public CorrosionQueenEntity(EntityType<? extends CorrosionQueenEntity> type, Level level) {
        super(type, level);
        this.xpReward = 12;
        if (this.getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    @Override
    protected String defaultTexture() {
        return "corrosion_queen";
    }

    @Override
    protected RawAnimation movementAnimation(AnimationState<?> event) {
        return moving(event) ? loop("walk") : loop("idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, this.meleeGoal(1.2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(6, new BreakDoorGoal(this, difficulty -> true));
        this.goalSelector.addGoal(7, new PanicGoal(this, 1.2));
        this.targetSelector.addGoal(8, target(this, Player.class));
        this.targetSelector.addGoal(9, target(this, HazmatLeaderEntity.class));
        this.targetSelector.addGoal(10, target(this, HazmatGuardEntity.class));
        this.targetSelector.addGoal(11, target(this, HazmatFlamethrowerEntity.class));
        this.targetSelector.addGoal(12, target(this, HazmatHelicopterEntity.class));
        this.targetSelector.addGoal(13, target(this, IronGolem.class));
        this.targetSelector.addGoal(14, target(this, Villager.class));
        this.targetSelector.addGoal(15, target(this, HazmatMedicEntity.class));
        this.targetSelector.addGoal(16, target(this, ScientistEntity.class));
        MutantEntity.addPreyTarget(this, this.targetSelector, 30);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.CORROSION_QUEEN_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.HUSK_STEP, 0.15F, 1.0F);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.CORROSION_QUEEN_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.CORROSION_QUEEN_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud
                || source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 45.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }
}
