package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    public static final ModRegistry<SoundEvent> REGISTRY = ModRegistry.create(Registries.SOUND_EVENT);

    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_BEAR_AMBIENT = register("entity.assimilated_bear.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_BEAR_DEATH = register("entity.assimilated_bear.death");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_COW_AMBIENT = register("entity.assimilated_cow.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_CREEPER_AMBIENT = register("entity.assimilated_creeper.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_DONKEY_AMBIENT = register("entity.assimilated_donkey.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_ENDERMAN_AMBIENT = register("entity.assimilated_enderman.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_ENDERMAN_DEATH = register("entity.assimilated_enderman.death");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_ENDERMAN_HURT = register("entity.assimilated_enderman.hurt");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_ENDERMAN_TELEPORT = register("entity.assimilated_enderman.teleport");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_ENDERMAN_TELEPORT_FAR = register("entity.assimilated_enderman.teleport_far");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_EVOKER_AMBIENT = register("entity.assimilated_evoker.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_EVOKER_SUMMON_FANGS = register("entity.assimilated_evoker.summon_fangs");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_EVOKER_SUMMON_VEX = register("entity.assimilated_evoker.summon_vex");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_FOX_AMBIENT = register("entity.assimilated_fox.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_HORSE_AMBIENT = register("entity.assimilated_horse.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_PIG_AMBIENT = register("entity.assimilated_pig.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_SHEEP_AMBIENT = register("entity.assimilated_sheep.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_SPIDER_AMBIENT = register("entity.assimilated_spider.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_WITCH_AMBIENT = register("entity.assimilated_witch.ambient");
    public static final ModRegistry.Entry<SoundEvent> ASSIMILATED_WOLF_AMBIENT = register("entity.assimilated_wolf.ambient");
    public static final ModRegistry.Entry<SoundEvent> CARNIVORAE_AMBIENT = register("entity.carnivorae.ambient");
    public static final ModRegistry.Entry<SoundEvent> CORROSION_QUEEN_AMBIENT = register("entity.corrosion_queen.ambient");
    public static final ModRegistry.Entry<SoundEvent> CORROSION_QUEEN_DEATH = register("entity.corrosion_queen.death");
    public static final ModRegistry.Entry<SoundEvent> CORROSION_QUEEN_HURT = register("entity.corrosion_queen.hurt");
    public static final ModRegistry.Entry<SoundEvent> CORROSION_QUEEN_SPELL = register("entity.corrosion_queen.spell");
    public static final ModRegistry.Entry<SoundEvent> CORROSION_QUEEN_SUMMON_BLOOD_SPIKE = register("entity.corrosion_queen.summon_blood_spike");
    public static final ModRegistry.Entry<SoundEvent> CORROSION_QUEEN_SUMMON_POISONED_SPIKES = register("entity.corrosion_queen.summon_poisoned_spikes");
    public static final ModRegistry.Entry<SoundEvent> HAZMAT_FLAMETHROWER_EXPLODE = register("entity.hazmat_flamethrower.explode");
    public static final ModRegistry.Entry<SoundEvent> HAZMAT_HELICOPTER_AMBIENT = register("entity.hazmat_helicopter.ambient");
    public static final ModRegistry.Entry<SoundEvent> HOOK_AMBIENT = register("entity.hook.ambient");
    public static final ModRegistry.Entry<SoundEvent> HUMAN_HERDER_AMBIENT = register("entity.human_herder.ambient");
    public static final ModRegistry.Entry<SoundEvent> HUMAN_HERDER_STEP = register("entity.human_herder.step");
    public static final ModRegistry.Entry<SoundEvent> HUMAN_STAGE_AMBIENT = register("entity.human_stage.ambient");
    public static final ModRegistry.Entry<SoundEvent> MUTANT_ANIMAL_DEATH = register("entity.mutant.animal_death");
    public static final ModRegistry.Entry<SoundEvent> MUTANT_BITE = register("entity.mutant.bite");
    public static final ModRegistry.Entry<SoundEvent> MUTANT_DEATH = register("entity.mutant.death");
    public static final ModRegistry.Entry<SoundEvent> MUTANT_HUMAN_AMBIENT = register("entity.mutant.human_ambient");
    public static final ModRegistry.Entry<SoundEvent> MUTANT_HURT = register("entity.mutant.hurt");
    public static final ModRegistry.Entry<SoundEvent> MUTANT_TRANSFORM = register("entity.mutant.transform");
    public static final ModRegistry.Entry<SoundEvent> NECROPTOR_AMBIENT = register("entity.necroptor.ambient");
    public static final ModRegistry.Entry<SoundEvent> PARASITIC_SHOOTER_AMBIENT = register("entity.parasitic_shooter.ambient");
    public static final ModRegistry.Entry<SoundEvent> REDUCTOR_AMBIENT = register("entity.reductor.ambient");
    public static final ModRegistry.Entry<SoundEvent> RESENTER_AMBIENT = register("entity.resenter.ambient");
    public static final ModRegistry.Entry<SoundEvent> THE_INTOXICATOR_AMBIENT = register("entity.the_intoxicator.ambient");
    public static final ModRegistry.Entry<SoundEvent> THE_INTOXICATOR_SCREAM = register("entity.the_intoxicator.scream");
    public static final ModRegistry.Entry<SoundEvent> VILLAGE_ALARM = register("event.village.alarm");
    public static final ModRegistry.Entry<SoundEvent> VILLAGE_AMBULANCE = register("event.village.ambulance");
    public static final ModRegistry.Entry<SoundEvent> ITEM_FLAMETHROWER_BURN = register("item.flamethrower.burn");

    public static final ModRegistry.Entry<SoundEvent> FLAMETHROWER_USE = register("item.flamethrower.use", 32.0F);
    public static final ModRegistry.Entry<SoundEvent> FLAMETHROWER_BUBBLE = register("item.flamethrower.bubble");
    public static final ModRegistry.Entry<SoundEvent> FLAMETHROWER_OVERHEAT = register("item.flamethrower.overheat", 32.0F);

    private static ModRegistry.Entry<SoundEvent> register(String name) {
        return REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(ModUtil.id(MutationcraftMod.MODID, name)));
    }

    private static ModRegistry.Entry<SoundEvent> register(String name, float range) {
        return REGISTRY.register(name, () -> SoundEvent.createFixedRangeEvent(ModUtil.id(MutationcraftMod.MODID, name), range));
    }

    private ModSounds() {
    }
}
