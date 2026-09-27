package com.asestefan.mutationcraft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public final class MutationcraftConfig {
    public static final List<Entry> ENTRIES = new ArrayList<>();

    public static final Toggle MOBS_SPAWN_NATURALLY = toggle("spawning", "mobsSpawnNaturally", "Mutants spawn naturally in the world.", true);
    public static final Toggle HAZMATS_SPAWN_NATURALLY = toggle("spawning", "hazmatsSpawnNaturally", "Hazmats spawn naturally.", true);
    public static final Toggle HELICOPTER_SPAWNS_NATURALLY = toggle("spawning", "helicopterSpawnsNaturally", "Hazmat Helicopters spawn naturally.", true);
    public static final Toggle FLAMETHROWER_SPAWNS_NATURALLY = toggle("spawning", "flamethrowerSpawnsNaturally", "Hazmat Flamethrowers spawn naturally.", true);

    public static final Number STARTING_STAGE = number("stages", "startingStage", "Spawning stage new worlds start at.", 0.0, 0.0, 4.0);
    public static final Toggle OUTBREAK_TIMER_RUNS = toggle("stages", "outbreakTimerRuns", "The outbreak timer counts up over time.", true);
    public static final Number OUTBREAK_TIMER_SPEED = number("stages", "outbreakTimerSpeed", "How fast the outbreak timer counts up.", 1.0, 0.01, 100.0);
    public static final Number UNLOCK_TIME_MULTIPLIER = number("stages", "unlockTimeMultiplier", "Multiplier for how long each mutant tier takes to unlock.", 1.0, 0.0, 100.0);
    public static final Toggle COMMON_MUTANTS_SPAWN = toggle("stages", "commonMutantsSpawn", "Common mutants can spawn.", true);
    public static final Toggle PARASITES_SPAWN = toggle("stages", "parasitesSpawn", "Necroptors and Reductors can spawn.", true);
    public static final Toggle ADVANCED_MUTANTS_SPAWN = toggle("stages", "advancedMutantsSpawn", "Advanced mutants can spawn.", true);
    public static final Toggle ELITE_MUTANTS_SPAWN = toggle("stages", "eliteMutantsSpawn", "Elite mutants can spawn.", true);
    public static final Toggle BOSS_MUTANTS_SPAWN = toggle("stages", "bossMutantsSpawn", "Boss mutants can spawn.", true);

    public static final Toggle HUMAN_STAGES_EVOLVE = toggle("evolution", "humanStagesEvolve", "Human Stage 1 and 2 evolve into the next stage over time.", true);
    public static final Number HUMAN_STAGE_EVOLVE_SECONDS = number("evolution", "humanStageEvolveSeconds", "Seconds a Human Stage needs to evolve.", 300.0, 1.0, 100000.0);
    public static final Toggle MITER_EVOLVES = toggle("evolution", "miterEvolvesIntoNecroptor", "Miters evolve into Necroptors over time.", true);
    public static final Number MITER_EVOLVE_SECONDS = number("evolution", "miterEvolveSeconds", "Seconds a Miter needs to evolve.", 120.0, 1.0, 100000.0);
    public static final Toggle ROAMER_DEVELOPS = toggle("evolution", "assimilatedRoamerDevelops", "Assimilated Roamers develop into Developed Roamers.", true);
    public static final Number ROAMER_DEVELOP_SECONDS = number("evolution", "roamerDevelopSeconds", "Seconds an Assimilated Roamer needs to develop.", 120.0, 1.0, 100000.0);
    public static final Toggle NECROPTOR_EVOLVES = toggle("evolution", "necroptorEvolves", "Necroptors evolve into Reductors after killing Zombies.", true);
    public static final Number NECROPTOR_EVOLVE_KILLS = number("evolution", "necroptorEvolveKills", "Zombies a Necroptor must kill to evolve.", 5.0, 1.0, 1000.0);
    public static final Number HUMAN_EVOLVE_KILLS = number("evolution", "assimilatedHumanEvolveKills", "Skeletons an Assimilated Human must kill to become a Flayer.", 5.0, 1.0, 1000.0);

    public static final Toggle HOOKS_EVOLVE = toggle("hooks", "hooksEvolve", "Light Hooks evolve into Medium Hooks and Medium Hooks into Heavy Hooks.", true);
    public static final Number LIGHT_HOOK_EVOLVE_SECONDS = number("hooks", "lightHookEvolveSeconds", "Seconds a Light Hook needs to evolve.", 300.0, 1.0, 100000.0);
    public static final Number MEDIUM_HOOK_EVOLVE_SECONDS = number("hooks", "mediumHookEvolveSeconds", "Seconds a Medium Hook needs to evolve.", 600.0, 1.0, 100000.0);
    public static final Toggle HOOKS_SUMMON_MOBS = toggle("hooks", "hooksSummonMobs", "Hooks summon Miters and Necroptors.", true);
    public static final Toggle HOOKS_PLACE_PUTRID_BLOCKS = toggle("hooks", "hooksPlacePutridBlocks", "Hooks place a Putrid Block under them when they spawn.", true);
    public static final Toggle HOOKS_FROM_NECROPTORS = toggle("hooks", "hooksFromNecroptors", "A Light Hook can spawn when a Necroptor dies.", true);
    public static final Number HOOK_FROM_NECROPTOR_CHANCE = number("hooks", "hookFromNecroptorChance", "Chance a Light Hook spawns when a Necroptor dies.", 0.05, 0.0, 1.0);

    public static final Toggle HOSTILES_FIGHT_MUTANTS = toggle("mobs", "hostilesFightMutants", "Hostile mobs from vanilla and other mods attack mutants on sight.", true);
    public static final Toggle ENDERMAN_BRINGS_MUTANTS = toggle("mobs", "endermanBringsMutants", "Assimilated Endermen teleport other mutants to you.", true);
    public static final Toggle ENDERMAN_TELEPORTS_TO_YOU = toggle("mobs", "endermanTeleportsToYou", "Assimilated Endermen teleport to you when they see you.", true);
    public static final Toggle VILLAGE_SIRENS = toggle("mobs", "villageSirens", "Villages sound alarms and sirens while mutants are inside them.", false);
    public static final Number EVOKER_VEX_LIMIT = number("mobs", "evokerVexLimit", "Vexes an Assimilated Evoker keeps around itself.", 6.0, 0.0, 100.0);
    public static final Number CARNIVORAE_MITER_LIMIT = number("mobs", "carnivoraeMiterLimit", "Miters a Carnivorae keeps around itself.", 4.0, 0.0, 100.0);
    public static final Number INTOXICATOR_NECROPTOR_LIMIT = number("mobs", "intoxicatorNecroptorLimit", "Necroptors The Intoxicator keeps around itself.", 6.0, 0.0, 100.0);
    public static final Number HELICOPTER_LIFETIME_SECONDS = number("mobs", "helicopterLifetimeSeconds", "Seconds a Hazmat Helicopter stays before leaving.", 180.0, 1.0, 100000.0);
    public static final Number HELICOPTER_MAX_DROPS = number("mobs", "helicopterMaxDrops", "Hazmats a Hazmat Helicopter can drop.", 8.0, 0.0, 100.0);
    public static final Toggle BURNING_MUTANTS_TAKE_MORE_DAMAGE = toggle("mobs", "burningMutantsTakeMoreDamage", "Burning mutants take extra damage.", true);
    public static final Number BURNING_MUTANT_DAMAGE_MULTIPLIER = number("mobs", "burningMutantDamageMultiplier", "Damage multiplier for burning mutants.", 2.0, 1.0, 100.0);
    public static final Number HAZMAT_FLAMETHROWER_DAMAGE = number("mobs", "hazmatFlamethrowerDamage", "Damage of each Hazmat Flamethrower flame burst.", 1.0, 0.0, 1000.0);
    public static final Number HAZMAT_FLAMETHROWER_EXPLODE_CHANCE = number("mobs", "hazmatFlamethrowerExplodeChance", "Chance a Hazmat Flamethrower explodes when it dies.", 0.5, 0.0, 1.0);
    public static final Number HAZMAT_FLAMETHROWER_FIRE_CHANCE = number("mobs", "hazmatFlamethrowerFireChance", "Chance a Hazmat Flamethrower flame burst lights blocks on fire.", 0.05, 0.0, 1.0);
    public static final Number MUTANT_HEALTH_MULTIPLIER = number("mobs", "mutantHealthMultiplier", "Health multiplier for mutants.", 1.0, 0.1, 100.0);
    public static final Number MUTANT_DAMAGE_MULTIPLIER = number("mobs", "mutantDamageMultiplier", "Attack damage multiplier for mutants.", 1.0, 0.0, 100.0);
    public static final Number MUTANT_SPEED_MULTIPLIER = number("mobs", "mutantSpeedMultiplier", "Movement speed multiplier for mutants.", 1.0, 0.1, 10.0);
    public static final Toggle MUTANTS_CONVERT_MOBS = toggle("mobs", "mutantsConvertMobs", "Mobs killed by mutants turn into mutants.", true);
    public static final Toggle MUTANTS_SPAWN_PARASITES = toggle("mobs", "mutantsSpawnParasites", "Mutants can release Miters or a Necroptor when they die.", true);
    public static final Toggle KILLING_MUTANTS_GIVES_SICKNESS = toggle("mobs", "killingMutantsGivesSickness", "Killing a mutant gives Mutagen Sickness.", true);
    public static final Toggle ASSIMILATED_CREEPER_EXPLODES = toggle("mobs", "assimilatedCreeperExplodes", "Assimilated Creepers explode when low on health.", true);
    public static final Number ASSIMILATED_CREEPER_EXPLOSION_POWER = number("mobs", "assimilatedCreeperExplosionPower", "Explosion power of the Assimilated Creeper.", 5.0, 0.0, 20.0);
    public static final Number NECROPTOR_SLOWNESS_CHANCE = number("mobs", "necroptorSlownessChance", "Chance a Necroptor attack gives Slowness.", 0.2, 0.0, 1.0);

    public static final Toggle PUTRID_BLOCK_SPREADING = toggle("blocks", "putridBlockSpreading", "Putrid Blocks spread to nearby blocks.", true);

    public static final Toggle FLAMETHROWER_NEEDS_FUEL = toggle("items", "flamethrowerNeedsFuel", "The Flamethrower needs fuel to function.", true);
    public static final Number FLAMETHROWER_TANK_CAPACITY = number("items", "flamethrowerTankCapacity", "How much fuel a Flamethrower tank can hold in seconds.", 120.0, 1.0, 100000.0);
    public static final Number FLAMETHROWER_FUEL_MULTIPLIER = number("items", "flamethrowerFuelMultiplier", "Multiplier for how long each fuel item lasts.", 1.0, 0.01, 100.0);
    public static final Toggle FLAMETHROWER_OVERHEATS = toggle("items", "flamethrowerOverheats", "The Flamethrower can overheat.", true);
    public static final Number FLAMETHROWER_OVERHEAT_SECONDS = number("items", "flamethrowerOverheatSeconds", "Seconds of nonstop spraying before the Flamethrower overheats.", 5.0, 0.5, 1000.0);
    public static final Number FLAMETHROWER_COOLDOWN_SECONDS = number("items", "flamethrowerCooldownSeconds", "How long in seconds the Flamethrower needs to wait after overheating.", 5.0, 0.5, 1000.0);
    public static final Toggle FLAMETHROWER_USES_DURABILITY = toggle("items", "flamethrowerUsesDurability", "The Flamethrower loses durability while spraying.", true);
    public static final Number FLAMETHROWER_DAMAGE = number("items", "flamethrowerDamage", "Damage of each Flamethrower flame burst.", 1.5, 0.0, 1000.0);
    public static final Number FLAMETHROWER_RANGE = number("items", "flamethrowerRange", "Blocks the Flamethrower flames reach.", 8.0, 1.0, 32.0);
    public static final Number FLAMETHROWER_BURN_SECONDS = number("items", "flamethrowerBurnSeconds", "How long in seconds a target hit by flames is set on fire for.", 5.0, 0.0, 1000.0);
    public static final Toggle FLAMETHROWER_LIGHTS_FIRES = toggle("items", "flamethrowerLightsFires", "The Flamethrower lights blocks on fire.", true);
    public static final Toggle FLAMETHROWER_BURNS_FOLIAGE = toggle("items", "flamethrowerBurnsFoliage", "The Flamethrower breaks leaves and other foliage like grass and flowers.", true);
    public static final Number FLAMETHROWER_MELEE_FIRE_CHANCE = number("items", "flamethrowerMeleeFireChance", "Chance a Flamethrower melee hit sets the target on fire.", 0.2, 0.0, 1.0);

    public static final Toggle PLAYER_BECOMES_ROAMER = toggle("players", "playerBecomesRoamer", "Players who die with Mutagen Sickness turn into an Assimilated Roamer.", true);

    public static final Toggle MOB_DROPS = toggle("drops", "mobDrops", "Mutants drop their loot.", true);

    private static Toggle toggle(String section, String name, String comment, boolean defaultValue) {
        Toggle toggle = new Toggle(section, name, comment, defaultValue);
        ENTRIES.add(toggle);
        return toggle;
    }

    private static Number number(String section, String name, String comment, double defaultValue, double min, double max) {
        Number number = new Number(section, name, comment, defaultValue, min, max);
        ENTRIES.add(number);
        return number;
    }

    public abstract static class Entry {
        public final String section;
        public final String name;
        public final String comment;

        private Entry(String section, String name, String comment) {
            this.section = section;
            this.name = name;
            this.comment = comment;
        }
    }

    public static final class Toggle extends Entry {
        public final boolean defaultValue;
        private BooleanSupplier value;

        private Toggle(String section, String name, String comment, boolean defaultValue) {
            super(section, name, comment);
            this.defaultValue = defaultValue;
        }

        public void bind(BooleanSupplier value) {
            this.value = value;
        }

        public boolean get() {
            if (value == null) {
                return defaultValue;
            }
            try {
                return value.getAsBoolean();
            } catch (IllegalStateException e) {
                return defaultValue;
            }
        }
    }

    public static final class Number extends Entry {
        public final double defaultValue;
        public final double min;
        public final double max;
        private DoubleSupplier value;

        private Number(String section, String name, String comment, double defaultValue, double min, double max) {
            super(section, name, comment);
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
        }

        public void bind(DoubleSupplier value) {
            this.value = value;
        }

        public double get() {
            if (value == null) {
                return defaultValue;
            }
            try {
                return value.getAsDouble();
            } catch (IllegalStateException e) {
                return defaultValue;
            }
        }

        public int getInt() {
            return (int) Math.round(get());
        }

        public int ticks() {
            return (int) Math.round(get() * 20.0);
        }
    }

    private MutationcraftConfig() {
    }
}
