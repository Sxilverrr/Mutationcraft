package com.asestefan.mutationcraft.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class HeavyHookEntity extends HookEntity {
    public HeavyHookEntity(EntityType<? extends HeavyHookEntity> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    @Override
    public Supplier<? extends EntityType<? extends Mob>> grownType() {
        return null;
    }

    @Override
    public String growthKey() {
        return "heavy";
    }

    @Override
    public int growthTicks() {
        return 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 90.0)
                .add(Attributes.ARMOR, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.6);
    }
}
