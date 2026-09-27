package com.asestefan.mutationcraft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public final class MutationcraftConfig {
    public static final List<Entry> ENTRIES = new ArrayList<>();

    public static final Toggle MOBS_SPAWN_NATURALLY = toggle("spawning", "mobsSpawnNaturally", "Mutants spawn naturally in the world.", true);
    public static final Toggle HELICOPTER_SPAWNS_NATURALLY = toggle("spawning", "helicopterSpawnsNaturally", "Hazmat Helicopters spawn naturally.", true);
    public static final Toggle FLAMETHROWER_SPAWNS_NATURALLY = toggle("spawning", "flamethrowerSpawnsNaturally", "Hazmat Flamethrowers spawn naturally.", true);

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

    public static final Toggle HOSTILES_FIGHT_MUTANTS = toggle("mobs", "hostilesFightMutants", "Hostile mobs from vanilla and other mods attack mutants on sight.", true);
    public static final Toggle ENDERMAN_BRINGS_MUTANTS = toggle("mobs", "endermanBringsMutants", "Assimilated Endermen teleport other mutants to you.", true);
    public static final Toggle ENDERMAN_TELEPORTS_TO_YOU = toggle("mobs", "endermanTeleportsToYou", "Assimilated Endermen teleport to you when they see you.", true);
    public static final Toggle VILLAGE_SIRENS = toggle("mobs", "villageSirens", "Villages sound alarms and sirens while mutants are inside them.", false);
    public static final Number EVOKER_VEX_LIMIT = number("mobs", "evokerVexLimit", "Vexes an Assimilated Evoker keeps around itself.", 6.0, 0.0, 100.0);
    public static final Number CARNIVORAE_MITER_LIMIT = number("mobs", "carnivoraeMiterLimit", "Miters a Carnivorae keeps around itself.", 4.0, 0.0, 100.0);
    public static final Number INTOXICATOR_NECROPTOR_LIMIT = number("mobs", "intoxicatorNecroptorLimit", "Necroptors The Intoxicator keeps around itself.", 6.0, 0.0, 100.0);
    public static final Number HELICOPTER_LIFETIME_SECONDS = number("mobs", "helicopterLifetimeSeconds", "Seconds a Hazmat Helicopter stays before leaving.", 180.0, 1.0, 100000.0);
    public static final Number HELICOPTER_MAX_DROPS = number("mobs", "helicopterMaxDrops", "Hazmats a Hazmat Helicopter can drop.", 8.0, 0.0, 100.0);

    public static final Toggle PUTRID_BLOCK_SPREADING = toggle("blocks", "putridBlockSpreading", "Putrid Blocks spread to nearby blocks.", true);

    public static final Number FLAMETHROWER_OVERHEAT_SECONDS = number("items", "flamethrowerOverheatSeconds", "Seconds of nonstop spraying before the Flamethrower overheats.", 5.0, 0.5, 1000.0);

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
