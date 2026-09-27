package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.init.ModEntities;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class LightHookEntity extends HookEntity {
    public LightHookEntity(EntityType<? extends LightHookEntity> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    @Override
    protected String defaultTexture() {
        return "light_hook";
    }

    @Override
    public Supplier<? extends EntityType<? extends Mob>> grownType() {
        return ModEntities.MEDIUM_HOOK;
    }

    @Override
    public String growthKey() {
        return "medium";
    }

    @Override
    public int growthTicks() {
        return MutationcraftConfig.LIGHT_HOOK_EVOLVE_SECONDS.ticks();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 35.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.2);
    }
}
