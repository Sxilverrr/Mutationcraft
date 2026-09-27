package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.behavior.Spawning;
import com.asestefan.mutationcraft.entity.*;
import com.asestefan.mutationcraft.platform.Services;
import com.asestefan.mutationcraft.registry.ModRegistry;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public final class ModEntities {
    public static final ModRegistry<EntityType<?>> REGISTRY = ModRegistry.create(Registries.ENTITY_TYPE);

    public static final ModRegistry.Entry<EntityType<NecroptorBombEntity>> NECROPTOR_BOMB = register("necroptor_bomb", EntityType.Builder.<NecroptorBombEntity>of(NecroptorBombEntity::new, MobCategory.MISC).clientTrackingRange(64).updateInterval(1).sized(0.5F, 0.5F));
    public static final ModRegistry.Entry<EntityType<AssimilatedVillagerEntity>> ASSIMILATED_VILLAGER = mob("assimilated_villager", AssimilatedVillagerEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedPillagerEntity>> ASSIMILATED_PILLAGER = mob("assimilated_pillager", AssimilatedPillagerEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<HumanStage1Entity>> HUMAN_STAGE_1 = mob("human_stage_1", HumanStage1Entity::new, 64, 1.5F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedPigEntity>> ASSIMILATED_PIG = mob("assimilated_pig", AssimilatedPigEntity::new, 64, 1.0F, 0.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedHorseEntity>> ASSIMILATED_HORSE = mob("assimilated_horse", AssimilatedHorseEntity::new, 64, 1.2F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedSpiderEntity>> ASSIMILATED_SPIDER = mob("assimilated_spider", AssimilatedSpiderEntity::new, 64, 1.0F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedJockeyEntity>> ASSIMILATED_JOCKEY = mob("assimilated_jockey", AssimilatedJockeyEntity::new, 64, 1.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedPiglinEntity>> ASSIMILATED_PIGLIN = mob("assimilated_piglin", AssimilatedPiglinEntity::new, 64, 1.0F, 1.8F);
    public static final ModRegistry.Entry<EntityType<HumanStage2Entity>> HUMAN_STAGE_2 = mob("human_stage_2", HumanStage2Entity::new, 64, 1.7F, 1.8F);
    public static final ModRegistry.Entry<EntityType<HumanStage3Entity>> HUMAN_STAGE_3 = mob("human_stage_3", HumanStage3Entity::new, 64, 1.7F, 1.8F);
    public static final ModRegistry.Entry<EntityType<NecroptorEntity>> NECROPTOR = mob("necroptor", NecroptorEntity::new, 64, 0.9F, 0.8F);
    public static final ModRegistry.Entry<EntityType<RottenSkeletonEntity>> ROTTEN_SKELETON = mob("rotten_skeleton", RottenSkeletonEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<TheIntoxicatorEntity>> THE_INTOXICATOR = mob("the_intoxicator", TheIntoxicatorEntity::new, 64, 2.3F, 2.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedWanderingTraderEntity>> ASSIMILATED_WANDERING_TRADER = mob("assimilated_wandering_trader", AssimilatedWanderingTraderEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<MiterEntity>> MITER = mob("miter", MiterEntity::new, 64, 0.4F, 0.3F);
    public static final ModRegistry.Entry<EntityType<AssimilatedHumanEntity>> ASSIMILATED_HUMAN = mob("assimilated_human", AssimilatedHumanEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<DevelopedRoamerEntity>> DEVELOPED_ROAMER = mob("developed_roamer", DevelopedRoamerEntity::new, 64, 1.0F, 2.4F);
    public static final ModRegistry.Entry<EntityType<AssimilatedRoamerEntity>> ASSIMILATED_ROAMER = mob("assimilated_roamer", AssimilatedRoamerEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedSheepEntity>> ASSIMILATED_SHEEP = mob("assimilated_sheep", AssimilatedSheepEntity::new, 64, 1.1F, 1.8F);
    public static final ModRegistry.Entry<EntityType<HazmatGuardEntity>> HAZMAT_GUARD = creature("hazmat_guard", HazmatGuardEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<ScientistEntity>> SCIENTIST = creature("scientist", ScientistEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedCowEntity>> ASSIMILATED_COW = mob("assimilated_cow", AssimilatedCowEntity::new, 64, 1.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedWitchEntity>> ASSIMILATED_WITCH = mob("assimilated_witch", AssimilatedWitchEntity::new, 64, 1.0F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedEndermanEntity>> ASSIMILATED_ENDERMAN = mob("assimilated_enderman", AssimilatedEndermanEntity::new, 156, 1.0F, 3.0F);
    public static final ModRegistry.Entry<EntityType<ReductorEntity>> REDUCTOR = mob("reductor", ReductorEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<HazmatHelicopterEntity>> HAZMAT_HELICOPTER = creature("hazmat_helicopter", HazmatHelicopterEntity::new, 128, 1.2F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedWolfEntity>> ASSIMILATED_WOLF = mob("assimilated_wolf", AssimilatedWolfEntity::new, 64, 1.3F, 0.8F);
    public static final ModRegistry.Entry<EntityType<HazmatLeaderEntity>> HAZMAT_LEADER = creature("hazmat_leader", HazmatLeaderEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<HazmatFlamethrowerEntity>> HAZMAT_FLAMETHROWER = register("hazmat_flamethrower", EntityType.Builder.<HazmatFlamethrowerEntity>of(HazmatFlamethrowerEntity::new, MobCategory.CREATURE).clientTrackingRange(64).updateInterval(3).fireImmune().sized(0.9F, 1.8F));
    public static final ModRegistry.Entry<EntityType<HazmatMedicEntity>> HAZMAT_MEDIC = creature("hazmat_medic", HazmatMedicEntity::new, 64, 0.9F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedFoxEntity>> ASSIMILATED_FOX = mob("assimilated_fox", AssimilatedFoxEntity::new, 64, 0.8F, 0.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedBearEntity>> ASSIMILATED_BEAR = mob("assimilated_bear", AssimilatedBearEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedEvokerEntity>> ASSIMILATED_EVOKER = mob("assimilated_evoker", AssimilatedEvokerEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedVexEntity>> ASSIMILATED_VEX = mob("assimilated_vex", AssimilatedVexEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<CarnivoraeEntity>> CARNIVORAE = mob("carnivorae", CarnivoraeEntity::new, 128, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<AssimilatedDonkeyEntity>> ASSIMILATED_DONKEY = mob("assimilated_donkey", AssimilatedDonkeyEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<FlayerEntity>> FLAYER = mob("flayer", FlayerEntity::new, 64, 1.2F, 2.2F);
    public static final ModRegistry.Entry<EntityType<CorrosionQueenEntity>> CORROSION_QUEEN = mob("corrosion_queen", CorrosionQueenEntity::new, 64, 1.0F, 2.4F);
    public static final ModRegistry.Entry<EntityType<PoisonedSpikesEntity>> POISONED_SPIKES = register("poisoned_spikes", EntityType.Builder.<PoisonedSpikesEntity>of(PoisonedSpikesEntity::new, MobCategory.MONSTER).clientTrackingRange(64).updateInterval(3).fireImmune().sized(0.6F, 1.4F));
    public static final ModRegistry.Entry<EntityType<BloodSpikeEntity>> BLOOD_SPIKE = register("blood_spike", EntityType.Builder.<BloodSpikeEntity>of(BloodSpikeEntity::new, MobCategory.MONSTER).clientTrackingRange(64).updateInterval(3).fireImmune().sized(0.6F, 1.4F));
    public static final ModRegistry.Entry<EntityType<HumanHerderEntity>> HUMAN_HERDER = mob("human_herder", HumanHerderEntity::new, 64, 0.9F, 4.2F);
    public static final ModRegistry.Entry<EntityType<ResenterEntity>> RESENTER = mob("resenter", ResenterEntity::new, 64, 1.2F, 4.0F);
    public static final ModRegistry.Entry<EntityType<AssimilatedCreeperEntity>> ASSIMILATED_CREEPER = mob("assimilated_creeper", AssimilatedCreeperEntity::new, 64, 0.8F, 2.0F);
    public static final ModRegistry.Entry<EntityType<ParasiticRamEntity>> PARASITIC_RAM = mob("parasitic_ram", ParasiticRamEntity::new, 64, 1.6F, 2.0F);
    public static final ModRegistry.Entry<EntityType<ParasiticRollerEntity>> PARASITIC_ROLLER = mob("parasitic_roller", ParasiticRollerEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<ParasiticShooterEntity>> PARASITIC_SHOOTER = mob("parasitic_shooter", ParasiticShooterEntity::new, 64, 1.3F, 2.4F);
    public static final ModRegistry.Entry<EntityType<PoisonedOrbEntity>> POISONED_ORB = register("poisoned_orb", EntityType.Builder.<PoisonedOrbEntity>of(PoisonedOrbEntity::new, MobCategory.MISC).clientTrackingRange(64).updateInterval(1).sized(0.5F, 0.5F));
    public static final ModRegistry.Entry<EntityType<LightHookEntity>> LIGHT_HOOK = mob("light_hook", LightHookEntity::new, 64, 0.6F, 1.8F);
    public static final ModRegistry.Entry<EntityType<MediumHookEntity>> MEDIUM_HOOK = mob("medium_hook", MediumHookEntity::new, 64, 1.2F, 2.8F);
    public static final ModRegistry.Entry<EntityType<HeavyHookEntity>> HEAVY_HOOK = mob("heavy_hook", HeavyHookEntity::new, 64, 1.8F, 4.2F);

    private static <T extends Entity> ModRegistry.Entry<EntityType<T>> mob(String name, EntityType.EntityFactory<T> factory, int range, float width, float height) {
        return register(name, EntityType.Builder.of(factory, MobCategory.MONSTER).clientTrackingRange(range).updateInterval(3).sized(width, height));
    }

    private static <T extends Entity> ModRegistry.Entry<EntityType<T>> creature(String name, EntityType.EntityFactory<T> factory, int range, float width, float height) {
        return register(name, EntityType.Builder.of(factory, MobCategory.CREATURE).clientTrackingRange(range).updateInterval(3).sized(width, height));
    }

    private static <T extends Entity> ModRegistry.Entry<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return REGISTRY.register(name, () -> builder.build(name));
    }

    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> consumer) {
        consumer.accept(ASSIMILATED_VILLAGER.get(), AssimilatedVillagerEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_PILLAGER.get(), AssimilatedPillagerEntity.createAttributes().build());
        consumer.accept(HUMAN_STAGE_1.get(), HumanStage1Entity.createAttributes().build());
        consumer.accept(ASSIMILATED_PIG.get(), AssimilatedPigEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_HORSE.get(), AssimilatedHorseEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_SPIDER.get(), AssimilatedSpiderEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_JOCKEY.get(), AssimilatedJockeyEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_PIGLIN.get(), AssimilatedPiglinEntity.createAttributes().build());
        consumer.accept(HUMAN_STAGE_2.get(), HumanStage2Entity.createAttributes().build());
        consumer.accept(HUMAN_STAGE_3.get(), HumanStage3Entity.createAttributes().build());
        consumer.accept(NECROPTOR.get(), NecroptorEntity.createAttributes().build());
        consumer.accept(ROTTEN_SKELETON.get(), RottenSkeletonEntity.createAttributes().build());
        consumer.accept(THE_INTOXICATOR.get(), TheIntoxicatorEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_WANDERING_TRADER.get(), AssimilatedWanderingTraderEntity.createAttributes().build());
        consumer.accept(MITER.get(), MiterEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_HUMAN.get(), AssimilatedHumanEntity.createAttributes().build());
        consumer.accept(DEVELOPED_ROAMER.get(), DevelopedRoamerEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_ROAMER.get(), AssimilatedRoamerEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_SHEEP.get(), AssimilatedSheepEntity.createAttributes().build());
        consumer.accept(HAZMAT_GUARD.get(), HazmatGuardEntity.createAttributes().build());
        consumer.accept(SCIENTIST.get(), ScientistEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_COW.get(), AssimilatedCowEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_WITCH.get(), AssimilatedWitchEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_ENDERMAN.get(), AssimilatedEndermanEntity.createAttributes().build());
        consumer.accept(REDUCTOR.get(), ReductorEntity.createAttributes().build());
        consumer.accept(HAZMAT_HELICOPTER.get(), HazmatHelicopterEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_WOLF.get(), AssimilatedWolfEntity.createAttributes().build());
        consumer.accept(HAZMAT_LEADER.get(), HazmatLeaderEntity.createAttributes().build());
        consumer.accept(HAZMAT_FLAMETHROWER.get(), HazmatFlamethrowerEntity.createAttributes().build());
        consumer.accept(HAZMAT_MEDIC.get(), HazmatMedicEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_FOX.get(), AssimilatedFoxEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_BEAR.get(), AssimilatedBearEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_EVOKER.get(), AssimilatedEvokerEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_VEX.get(), AssimilatedVexEntity.createAttributes().build());
        consumer.accept(CARNIVORAE.get(), CarnivoraeEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_DONKEY.get(), AssimilatedDonkeyEntity.createAttributes().build());
        consumer.accept(FLAYER.get(), FlayerEntity.createAttributes().build());
        consumer.accept(CORROSION_QUEEN.get(), CorrosionQueenEntity.createAttributes().build());
        consumer.accept(POISONED_SPIKES.get(), PoisonedSpikesEntity.createAttributes().build());
        consumer.accept(BLOOD_SPIKE.get(), BloodSpikeEntity.createAttributes().build());
        consumer.accept(HUMAN_HERDER.get(), HumanHerderEntity.createAttributes().build());
        consumer.accept(RESENTER.get(), ResenterEntity.createAttributes().build());
        consumer.accept(ASSIMILATED_CREEPER.get(), AssimilatedCreeperEntity.createAttributes().build());
        consumer.accept(PARASITIC_RAM.get(), ParasiticRamEntity.createAttributes().build());
        consumer.accept(PARASITIC_ROLLER.get(), ParasiticRollerEntity.createAttributes().build());
        consumer.accept(PARASITIC_SHOOTER.get(), ParasiticShooterEntity.createAttributes().build());
        consumer.accept(LIGHT_HOOK.get(), LightHookEntity.createAttributes().build());
        consumer.accept(MEDIUM_HOOK.get(), MediumHookEntity.createAttributes().build());
        consumer.accept(HEAVY_HOOK.get(), HeavyHookEntity.createAttributes().build());
    }

    public static void registerSpawnPlacements(SpawnPlacementRegistrar registrar) {
        common(registrar, ASSIMILATED_VILLAGER.get());
        common(registrar, ASSIMILATED_PILLAGER.get());
        common(registrar, ASSIMILATED_PIG.get());
        common(registrar, ASSIMILATED_HORSE.get());
        common(registrar, ASSIMILATED_SPIDER.get());
        common(registrar, ASSIMILATED_JOCKEY.get());
        common(registrar, ROTTEN_SKELETON.get());
        common(registrar, ASSIMILATED_WANDERING_TRADER.get());
        common(registrar, ASSIMILATED_HUMAN.get());
        common(registrar, DEVELOPED_ROAMER.get());
        common(registrar, ASSIMILATED_ROAMER.get());
        common(registrar, ASSIMILATED_SHEEP.get());
        common(registrar, ASSIMILATED_COW.get());
        common(registrar, ASSIMILATED_WITCH.get());
        common(registrar, ASSIMILATED_WOLF.get());
        common(registrar, ASSIMILATED_FOX.get());
        common(registrar, ASSIMILATED_BEAR.get());
        common(registrar, ASSIMILATED_DONKEY.get());
        registrar.register(ASSIMILATED_PIGLIN.get(), (type, world, reason, pos, random) -> Spawning.nether(world, Spawning.COMMON));
        registrar.register(NECROPTOR.get(), (type, world, reason, pos, random) -> Spawning.anywhere(world, Spawning.PARASITE));
        registrar.register(REDUCTOR.get(), (type, world, reason, pos, random) -> Spawning.anywhere(world, Spawning.PARASITE));
        staged(registrar, HUMAN_STAGE_1.get(), Spawning.ADVANCED);
        staged(registrar, HUMAN_STAGE_2.get(), Spawning.ADVANCED);
        staged(registrar, HUMAN_STAGE_3.get(), Spawning.ADVANCED);
        staged(registrar, ASSIMILATED_ENDERMAN.get(), Spawning.ADVANCED);
        staged(registrar, ASSIMILATED_EVOKER.get(), Spawning.ADVANCED);
        staged(registrar, CARNIVORAE.get(), Spawning.ADVANCED);
        staged(registrar, ASSIMILATED_CREEPER.get(), Spawning.ELITE);
        staged(registrar, FLAYER.get(), Spawning.ELITE);
        staged(registrar, PARASITIC_RAM.get(), Spawning.ELITE);
        staged(registrar, PARASITIC_ROLLER.get(), Spawning.ELITE);
        staged(registrar, LIGHT_HOOK.get(), Spawning.ELITE);
        staged(registrar, MEDIUM_HOOK.get(), Spawning.ELITE);
        staged(registrar, CORROSION_QUEEN.get(), Spawning.BOSS);
        staged(registrar, HUMAN_HERDER.get(), Spawning.BOSS);
        staged(registrar, RESENTER.get(), Spawning.BOSS);
        staged(registrar, PARASITIC_SHOOTER.get(), Spawning.BOSS);
        staged(registrar, HEAVY_HOOK.get(), Spawning.BOSS);
        registrar.register(MITER.get(), (type, world, reason, pos, random) -> Spawning.always(world));
        registrar.register(HAZMAT_GUARD.get(), (type, world, reason, pos, random) -> Spawning.hazmat(world, reason));
        registrar.register(HAZMAT_LEADER.get(), (type, world, reason, pos, random) -> Spawning.hazmat(world, reason));
        registrar.register(HAZMAT_MEDIC.get(), (type, world, reason, pos, random) -> Spawning.hazmat(world, reason));
        registrar.register(SCIENTIST.get(), (type, world, reason, pos, random) -> Spawning.hazmat(world, reason));
        registrar.register(HAZMAT_FLAMETHROWER.get(), (type, world, reason, pos, random) -> Spawning.hazmatFlamethrower(world, reason));
        registrar.register(HAZMAT_HELICOPTER.get(), (type, world, reason, pos, random) -> Spawning.helicopter(world, reason));
    }

    private static <T extends Mob> void common(SpawnPlacementRegistrar registrar, EntityType<T> type) {
        staged(registrar, type, Spawning.COMMON);
    }

    private static <T extends Mob> void staged(SpawnPlacementRegistrar registrar, EntityType<T> type, double[] thresholds) {
        registrar.register(type, (entityType, world, reason, pos, random) -> Spawning.overworld(world, thresholds));
    }

    public static void commonSetup() {
        Services.PLATFORM.addDungeonMob(MITER.get(), 180);
    }

    public interface SpawnPlacementRegistrar {
        <T extends Mob> void register(EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate);
    }

    private ModEntities() {
    }
}
