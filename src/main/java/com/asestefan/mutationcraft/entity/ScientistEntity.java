package com.asestefan.mutationcraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import com.asestefan.mutationcraft.init.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScientistEntity extends HazmatMob {
    public ScientistEntity(EntityType<? extends ScientistEntity> type, Level level) {
        super(type, level);
        this.xpReward = 5;
        if (this.getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MUTAGEN_SERUM.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.MUTAGEN_SERUM.get()));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.level() instanceof ServerLevel level) {
            scientistDeath(level, this.getX(), this.getY(), this.getZ());
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 20.0);
    }

    private static void scientistDeath(ServerLevel level, double x, double y, double z) {
        if (level.getRandom().nextDouble() <= 0.2) {
            level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }
}
