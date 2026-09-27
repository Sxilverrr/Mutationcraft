package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModEntities;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class MediumHookEntity extends HookEntity {
    public MediumHookEntity(EntityType<? extends MediumHookEntity> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    @Override
    public Supplier<? extends EntityType<? extends Mob>> grownType() {
        return ModEntities.HEAVY_HOOK;
    }

    @Override
    public String growthKey() {
        return "heavy";
    }

    @Override
    public int growthTicks() {
        return MutationcraftConfig.MEDIUM_HOOK_EVOLVE_SECONDS.ticks();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.6);
    }
}
