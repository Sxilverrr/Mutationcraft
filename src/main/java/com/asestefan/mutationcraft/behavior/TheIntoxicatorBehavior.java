package com.asestefan.mutationcraft.behavior;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.TheIntoxicatorEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class TheIntoxicatorBehavior {
    private static final String TOUCH_KEY = "mutationcraft:touch_cooldown";
    private static final double NECROPTOR_RANGE = 32.0;

    public static void onSpawn(TheIntoxicatorEntity boss) {
        boss.playAnimation("spawn");
        if (boss.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ENCHANT, boss.getX(), boss.getY(), boss.getZ(), 5, 0.0, 0.0, 0.0, 1.0);
        }
    }

    public static void tick(TheIntoxicatorEntity boss) {
        if (!(boss.level() instanceof ServerLevel level) || !boss.isAlive()) {
            return;
        }
        double r1 = level.getRandom().nextDouble();
        double r2 = level.getRandom().nextDouble();
        if (r1 > 0.02 || r2 > 0.1) {
            return;
        }
        double offset = r1 <= 0.01 ? 1.0 : -1.0;
        for (int i = 0; i < 2; i++) {
            if (level.getEntities(ModEntities.NECROPTOR.get(), boss.getBoundingBox().inflate(NECROPTOR_RANGE), Entity::isAlive).size() >= MutationcraftConfig.INTOXICATOR_NECROPTOR_LIMIT.getInt()) {
                return;
            }
            Mob necroptor = ModEntities.NECROPTOR.get().create(level);
            if (necroptor == null) {
                return;
            }
            double x = boss.getX() + offset;
            double z = boss.getZ() + offset;
            necroptor.moveTo(x, boss.getY(), z, level.getRandom().nextFloat() * 360.0F, 0.0F);
            if (ModUtil.teleportNear(necroptor, x, boss.getY(), z)) {
                ModUtil.finalizeSpawn(necroptor, level, level.getCurrentDifficultyAt(necroptor.blockPosition()), MobSpawnType.MOB_SUMMONED);
                level.addFreshEntity(necroptor);
            }
        }
    }

    public static void onTouch(TheIntoxicatorEntity boss, Player player) {
        if (boss.level().isClientSide() || !boss.isAlive() || player.isCreative() || player.isSpectator()
                || !ModUtil.tryCooldown(boss, TOUCH_KEY, 20)) {
            return;
        }
        boss.playAnimation("attack");
        player.hurt(boss.damageSources().mobAttack(boss), 0.5F);
    }

    public static void onAttack(LivingEntity victim) {
        if (victim.getRandom().nextDouble() <= 0.5) {
            victim.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        }
    }

    public static void onDeath(TheIntoxicatorEntity boss, Entity killer) {
        if (!(boss.level() instanceof ServerLevel level) || killer == null) {
            return;
        }
        double x = boss.getX();
        double y = boss.getY();
        double z = boss.getZ();
        level.sendParticles(ParticleTypes.ENCHANT, x, y, z, 5, 3.0, 3.0, 3.0, 1.0);
        level.playSound(null, x, y, z, ModSounds.THE_INTOXICATOR_SCREAM.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning != null) {
            lightning.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
            lightning.setVisualOnly(true);
            level.addFreshEntity(lightning);
        }
        if (killer instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), 2000, 0));
        }
    }

    private TheIntoxicatorBehavior() {
    }
}
