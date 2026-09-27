package com.asestefan.mutationcraft.entity;

import com.asestefan.mutationcraft.ModUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PoisonedSpikesEntity extends BloodSpikeEntity {
    public PoisonedSpikesEntity(EntityType<? extends PoisonedSpikesEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected int lifetime() {
        return 200;
    }

    @Override
    protected void ambientParticles(ServerLevel level) {
        if (this.random.nextDouble() <= 0.05) {
            level.sendParticles(ModUtil.entityEffect(), this.getX(), this.getY(), this.getZ(), 1, 1.0, 3.0, 1.0, 1.0);
        }
    }

    @Override
    protected void touch(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BloodSpikeEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 0.0);
    }
}
