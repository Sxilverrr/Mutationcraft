package com.asestefan.mutationcraft.command;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.behavior.Spawning;
import com.asestefan.mutationcraft.init.ModMobEffects;
import com.asestefan.mutationcraft.network.ModVariables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class ModCommands {
    private static final int DEFAULT_INFECTION_SECONDS = 300;
    private static final String[] TIERS = {"common", "advanced", "elite", "boss"};
    private static final double[][] THRESHOLDS = {Spawning.COMMON, Spawning.ADVANCED, Spawning.ELITE, Spawning.BOSS};

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mutationcraft")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("stage")
                        .then(Commands.literal("get").executes(ModCommands::stageGet))
                        .then(Commands.literal("set")
                                .then(Commands.argument("stage", IntegerArgumentType.integer(0, 4))
                                        .executes(ctx -> stageSet(ctx, IntegerArgumentType.getInteger(ctx, "stage"))))))
                .then(Commands.literal("time")
                        .then(Commands.literal("get").executes(ModCommands::timeGet))
                        .then(Commands.literal("set")
                                .then(Commands.argument("time", DoubleArgumentType.doubleArg(0))
                                        .executes(ctx -> timeSet(ctx, DoubleArgumentType.getDouble(ctx, "time")))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("time", DoubleArgumentType.doubleArg())
                                        .executes(ctx -> timeSet(ctx, Math.max(0.0, ModVariables.time(overworld(ctx)) + DoubleArgumentType.getDouble(ctx, "time")))))))
                .then(Commands.literal("pause").executes(ctx -> setAdvancing(ctx, false)))
                .then(Commands.literal("resume").executes(ctx -> setAdvancing(ctx, true)))
                .then(Commands.literal("reset").executes(ModCommands::reset))
                .then(Commands.literal("infect")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(ctx -> infect(ctx, DEFAULT_INFECTION_SECONDS))
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 1000000))
                                        .executes(ctx -> infect(ctx, IntegerArgumentType.getInteger(ctx, "seconds"))))))
                .then(Commands.literal("cure")
                        .then(Commands.argument("targets", EntityArgument.entities()).executes(ModCommands::cure)))
                .then(Commands.literal("count").executes(ModCommands::count))
                .then(Commands.literal("purge")
                        .executes(ctx -> purge(ctx, -1))
                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, 100000))
                                .executes(ctx -> purge(ctx, IntegerArgumentType.getInteger(ctx, "radius"))))));
    }

    private static ServerLevel overworld(CommandContext<CommandSourceStack> ctx) {
        return ctx.getSource().getServer().overworld();
    }

    private static void reply(CommandSourceStack source, Component message, boolean broadcast) {
        source.sendSuccess(() -> message, broadcast);
    }

    private static String format(double time) {
        return String.format("%,.0f", time);
    }

    private static int stageGet(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = overworld(ctx);
        int stage = ModVariables.stage(level);
        double time = ModVariables.time(level);
        CommandSourceStack source = ctx.getSource();
        reply(source, Component.translatable("commands.mutationcraft.stage.current", stage), false);
        reply(source, Component.translatable("commands.mutationcraft.time.get", format(time)), false);
        if (ModVariables.paused(level)) {
            reply(source, Component.translatable("commands.mutationcraft.stage.paused"), false);
        }
        for (int i = 0; i < TIERS.length; i++) {
            Component tier = Component.translatable("commands.mutationcraft.tier." + TIERS[i]);
            double threshold = THRESHOLDS[i][Math.max(0, Math.min(stage, THRESHOLDS[i].length - 1))];
            if (time > threshold) {
                reply(source, Component.translatable("commands.mutationcraft.stage.spawning", tier), false);
            } else {
                reply(source, Component.translatable("commands.mutationcraft.stage.locked", tier, format(threshold)), false);
            }
        }
        return stage;
    }

    private static int stageSet(CommandContext<CommandSourceStack> ctx, int stage) {
        ModVariables.MapVariables variables = ModVariables.MapVariables.get(overworld(ctx));
        variables.stage = stage;
        variables.setDirty();
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.stage.set", stage), true);
        return stage;
    }

    private static int timeGet(CommandContext<CommandSourceStack> ctx) {
        double time = ModVariables.time(overworld(ctx));
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.time.get", format(time)), false);
        return (int) time;
    }

    private static int timeSet(CommandContext<CommandSourceStack> ctx, double time) {
        ModVariables.MapVariables variables = ModVariables.MapVariables.get(overworld(ctx));
        variables.time = time;
        variables.setDirty();
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.time.set", format(time)), true);
        return (int) time;
    }

    private static int setAdvancing(CommandContext<CommandSourceStack> ctx, boolean advancing) {
        ModVariables.MapVariables variables = ModVariables.MapVariables.get(overworld(ctx));
        variables.paused = !advancing;
        variables.setDirty();
        reply(ctx.getSource(), Component.translatable(advancing ? "commands.mutationcraft.resume" : "commands.mutationcraft.pause"), true);
        return 1;
    }

    private static int reset(CommandContext<CommandSourceStack> ctx) {
        ModVariables.MapVariables variables = ModVariables.MapVariables.get(overworld(ctx));
        variables.stage = 0;
        variables.paused = false;
        variables.time = 0.0;
        variables.setDirty();
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.reset"), true);
        return 1;
    }

    private static int infect(CommandContext<CommandSourceStack> ctx, int seconds) throws CommandSyntaxException {
        int count = 0;
        for (Entity entity : EntityArgument.getEntities(ctx, "targets")) {
            if (entity instanceof LivingEntity living && living.addEffect(new MobEffectInstance(ModMobEffects.MUTAGEN_SICKNESS.ref(), seconds * 20, 0))) {
                count++;
            }
        }
        int infected = count;
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.infect", infected), true);
        return infected;
    }

    private static int cure(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int count = 0;
        for (Entity entity : EntityArgument.getEntities(ctx, "targets")) {
            if (entity instanceof LivingEntity living && living.removeEffect(ModMobEffects.MUTAGEN_SICKNESS.ref())) {
                count++;
            }
        }
        int cured = count;
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.cure", cured), true);
        return cured;
    }

    private static List<Entity> mutants(ServerLevel level, Vec3 center, int radius) {
        List<Entity> found = new ArrayList<>();
        for (Entity entity : level.getAllEntities()) {
            if (entity.isAlive() && ModUtil.isMutant(entity) && (radius < 0 || entity.position().closerThan(center, radius))) {
                found.add(entity);
            }
        }
        return found;
    }

    private static int count(CommandContext<CommandSourceStack> ctx) {
        List<Entity> found = mutants(ctx.getSource().getLevel(), ctx.getSource().getPosition(), -1);
        Map<String, Integer> byType = new TreeMap<>();
        for (Entity entity : found) {
            byType.merge(entity.getType().getDescription().getString(), 1, Integer::sum);
        }
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.count", found.size()), false);
        byType.forEach((name, amount) -> reply(ctx.getSource(), Component.literal(" - " + name + ": " + amount), false));
        return found.size();
    }

    private static int purge(CommandContext<CommandSourceStack> ctx, int radius) {
        List<Entity> found = mutants(ctx.getSource().getLevel(), ctx.getSource().getPosition(), radius);
        found.forEach(Entity::discard);
        reply(ctx.getSource(), Component.translatable("commands.mutationcraft.purge", found.size()), true);
        return found.size();
    }

    private ModCommands() {
    }
}
