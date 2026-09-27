package com.asestefan.mutationcraft.event;

import com.asestefan.mutationcraft.platform.Services;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.behavior.FlameSpray;
import com.asestefan.mutationcraft.behavior.MutantStats;
import com.asestefan.mutationcraft.config.MutationcraftConfig;
import com.asestefan.mutationcraft.entity.AnimatedMutant;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.network.ModVariables;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class ModEvents {
    private static final List<EventHandler> HANDLERS = List.of(new AnimalEvents(), new HumanoidEvents(), new IllagerEvents(), new AberrationEvents(), new BossEvents(), new HazmatEvents(), new CommonEvents());

    public static void onLivingAttack(LivingEntity entity, DamageSource source) {
        if (entity.level().isClientSide() || !entity.isAlive() || ignoresHit(entity, source)) {
            return;
        }
        for (EventHandler handler : HANDLERS) {
            handler.onLivingAttack(entity, source);
        }
    }

    public static float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!entity.level().isClientSide() && ModUtil.isMutant(entity) && entity.isOnFire() && MutationcraftConfig.BURNING_MUTANTS_TAKE_MORE_DAMAGE.get()) {
            amount *= (float) MutationcraftConfig.BURNING_MUTANT_DAMAGE_MULTIPLIER.get();
        }
        for (EventHandler handler : HANDLERS) {
            amount = handler.onLivingHurt(entity, source, amount);
        }
        return amount;
    }

    public static void onLivingDeath(LivingEntity entity, DamageSource source) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (EventHandler handler : HANDLERS) {
            handler.onLivingDeath(entity, source);
        }
    }

    public static void onChangeTarget(LivingEntity entity, LivingEntity newTarget) {
        if (entity.level().isClientSide() || newTarget == null) {
            return;
        }
        for (EventHandler handler : HANDLERS) {
            handler.onChangeTarget(entity, newTarget);
        }
    }

    public static void onLivingTick(LivingEntity entity) {
        if (entity instanceof AnimatedMutant animated) {
            animated.syncAnimation();
        }
        for (EventHandler handler : HANDLERS) {
            handler.onLivingTick(entity);
        }
    }

    public static void onPlayerTick(Player player) {
        for (EventHandler handler : HANDLERS) {
            handler.onPlayerTick(player);
        }
    }

    public static void onLevelTick(Level level) {
        ModVariables.tickTime(level);
        if (level instanceof ServerLevel server) {
            FlameSpray.tick(server);
        }
        for (EventHandler handler : HANDLERS) {
            handler.onLevelTick(level);
        }
    }

    public static void onEntityJoin(Entity entity) {
        mutantEnemies(entity);
        MutantStats.onJoin(entity);
    }

    public static boolean onEntityInteract(Player player, Entity target, InteractionHand hand) {
        boolean handled = false;
        for (EventHandler handler : HANDLERS) {
            handled |= handler.onEntityInteract(player, target, hand);
        }
        return handled;
    }

    public static void onEffectExpired(LivingEntity entity, MobEffectInstance instance) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (EventHandler handler : HANDLERS) {
            handler.onEffectExpired(entity, instance);
        }
    }

    public static void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (EventHandler handler : HANDLERS) {
            handler.onEffectAdded(entity, instance);
        }
    }

    public static void onServerTick() {
        MutationcraftMod.onServerTick();
    }

    public static boolean dropsDisabled(LivingEntity entity) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return MutationcraftMod.MODID.equals(id.getNamespace()) && !MutationcraftConfig.MOB_DROPS.get();
    }

    public static boolean canApplyEffect(LivingEntity entity, MobEffect effect) {
        if (effect == ModMobEffects.MUTAGEN_SICKNESS.get() && ModUtil.isMutant(entity)) {
            return false;
        }
        return !(entity instanceof Player player && (player.isCreative() || player.isSpectator()) && effect.getCategory() == MobEffectCategory.HARMFUL && !fromCommand());
    }

    private static boolean fromCommand() {
        return StackWalker.getInstance().walk(frames -> frames.anyMatch(frame -> frame.getClassName().startsWith("net.minecraft.server.commands.")));
    }

    private static boolean ignoresHit(LivingEntity entity, DamageSource source) {
        if (entity.isInvulnerableTo(source) || entity.isDamageSourceBlocked(source) || entity.invulnerableTime > 10) {
            return true;
        }
        return entity instanceof Player player && player.getAbilities().invulnerable && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    private ModEvents() {
    }

    private static final int PRIORITY = 3;

    private static void mutantEnemies(Entity entity) {
        if (!(entity instanceof Mob mob) || !(entity instanceof Enemy) || mob.level().isClientSide() || !MutationcraftConfig.HOSTILES_FIGHT_MUTANTS.get()
                || ModUtil.isMutant(mob) || MutationcraftMod.MODID.equals(BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).getNamespace())) {
            return;
        }
        GoalSelector targets = Services.PLATFORM.targetSelector(mob);
        for (var goal : targets.getAvailableGoals()) {
            if (goal.getGoal() instanceof HuntMutantsGoal) {
                return;
            }
        }
        targets.addGoal(PRIORITY, new HuntMutantsGoal(mob));
    }

    private static final class HuntMutantsGoal extends NearestAttackableTargetGoal<LivingEntity> {
        private HuntMutantsGoal(Mob mob) {
            super(mob, LivingEntity.class, 10, true, false, ModUtil::isMutant);
        }
    }
}
