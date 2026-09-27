package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.behavior.AnimalHurt;
import com.asestefan.mutationcraft.behavior.CarnivoraeBehavior;
import com.asestefan.mutationcraft.behavior.FlayerBehavior;
import com.asestefan.mutationcraft.behavior.NecroptorBehavior;
import com.asestefan.mutationcraft.entity.CarnivoraeEntity;
import com.asestefan.mutationcraft.entity.FlayerEntity;
import com.asestefan.mutationcraft.entity.HookEntity;
import com.asestefan.mutationcraft.entity.MiterEntity;
import com.asestefan.mutationcraft.entity.NecroptorEntity;
import com.asestefan.mutationcraft.entity.ReductorEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class AberrationEvents implements EventHandler {
    public AberrationEvents() {
    }

    @Override
    public void onLivingAttack(LivingEntity entity, DamageSource source) {
        NecroptorBehavior.onAttack(entity, source.getEntity());
        CarnivoraeBehavior.onAttack(entity, source.getEntity());
        FlayerBehavior.onAttacked(entity, source.getEntity());
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!entity.level().isClientSide()) {
            FlayerBehavior.onHurtTarget(entity, source.getEntity());
            if (entity instanceof MiterEntity || entity instanceof NecroptorEntity || entity instanceof ReductorEntity) {
                return amount + AnimalHurt.fireAspectBonus(source, false);
            }
            if (entity instanceof CarnivoraeEntity || entity instanceof FlayerEntity || entity instanceof HookEntity) {
                return amount + AnimalHurt.fireAspectBonus(source, true);
            }
        }
        return amount;
    }

    @Override
    public void onLivingDeath(LivingEntity entity, DamageSource source) {
        NecroptorBehavior.onAnyDeath(entity, source.getEntity());
    }
}
