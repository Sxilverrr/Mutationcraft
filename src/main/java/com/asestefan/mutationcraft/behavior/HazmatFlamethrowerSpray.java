package com.asestefan.mutationcraft.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class HazmatFlamethrowerSpray {
    public static final int BURST_TICKS = 30;
    public static final double BURST_RANGE = 10.0;
    private static final float DAMAGE = 1.0F;
    private static final double IGNITE_CHANCE = 0.05;

    public static Vec3 aim(Mob mob, LivingEntity target) {
        return target.getBoundingBox().getCenter().subtract(mob.getEyePosition().add(0.0, -0.4, 0.0)).normalize();
    }

    public static boolean canBurst(Mob mob, LivingEntity target) {
        if (!target.isAlive() || mob.distanceToSqr(target) > BURST_RANGE * BURST_RANGE || !mob.getSensing().hasLineOfSight(target)) {
            return false;
        }
        Vec3 direction = aim(mob, target);
        return !FlameSpray.allyInStream(mob, FlameSpray.tip(mob, direction), direction, Math.min(BURST_RANGE, mob.distanceTo(target)));
    }

    public static void tick(Mob mob, LivingEntity target, int tick) {
        if (!(mob.level() instanceof ServerLevel level)) {
            return;
        }
        mob.getLookControl().setLookAt(target, 60.0F, 60.0F);
        Vec3 direction = aim(mob, target);
        Vec3 origin = FlameSpray.tip(mob, direction);
        FlameSpray.particles(level, mob, origin, direction);
        if (tick % 3 == 0) {
            FlameSpray.burn(level, mob, origin, direction, DAMAGE, IGNITE_CHANCE, ItemStack.EMPTY);
        }
    }

    private HazmatFlamethrowerSpray() {
    }
}
