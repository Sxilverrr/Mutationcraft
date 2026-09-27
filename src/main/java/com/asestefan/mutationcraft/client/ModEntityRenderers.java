package com.asestefan.mutationcraft.client;

import com.asestefan.mutationcraft.entity.AnimatedMutant;
import com.asestefan.mutationcraft.entity.CorrosionQueenEntity;
import com.asestefan.mutationcraft.init.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public final class ModEntityRenderers {
    public static void register(RendererRegistrar registrar) {
        registrar.register(ModEntities.NECROPTOR_BOMB.get(), ThrownItemRenderer::new);
        registrar.register(ModEntities.POISONED_ORB.get(), ThrownItemRenderer::new);
        geo(registrar, ModEntities.ASSIMILATED_VILLAGER.get(), "assimilated_villager", 0.5F);
        registrar.register(ModEntities.ASSIMILATED_PILLAGER.get(), context -> new MutantGeoRenderer<>(context, "assimilated_pillager", 0.5F, false));
        geo(registrar, ModEntities.HUMAN_STAGE_1.get(), "human_stage_1", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_PIG.get(), "assimilated_pig", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_HORSE.get(), "assimilated_horse", 0.5F);
        registrar.register(ModEntities.ASSIMILATED_SPIDER.get(), context -> new MutantGeoRenderer<>(context, "assimilated_spider", 0.5F, false));
        registrar.register(ModEntities.ASSIMILATED_JOCKEY.get(), context -> new MutantGeoRenderer<>(context, "assimilated_jockey", 0.5F, false));
        geo(registrar, ModEntities.ASSIMILATED_PIGLIN.get(), "assimilated_piglin", 0.5F);
        geo(registrar, ModEntities.HUMAN_STAGE_2.get(), "human_stage_2", 0.5F);
        registrar.register(ModEntities.HUMAN_STAGE_3.get(), context -> new MutantGeoRenderer<>(context, "human_stage_3", 0.5F, false));
        geo(registrar, ModEntities.NECROPTOR.get(), "necroptor", 0.5F);
        geo(registrar, ModEntities.ROTTEN_SKELETON.get(), "rotten_skeleton", 0.5F);
        registrar.register(ModEntities.THE_INTOXICATOR.get(), context -> new MutantGeoRenderer<>(context, "the_intoxicator", 1.8F, false));
        geo(registrar, ModEntities.ASSIMILATED_WANDERING_TRADER.get(), "assimilated_wandering_trader", 0.5F);
        registrar.register(ModEntities.MITER.get(), MiterRenderer::new);
        geo(registrar, ModEntities.ASSIMILATED_HUMAN.get(), "assimilated_human", 0.5F);
        geo(registrar, ModEntities.DEVELOPED_ROAMER.get(), "developed_roamer", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_ROAMER.get(), "assimilated_roamer", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_SHEEP.get(), "assimilated_sheep", 0.5F);
        registrar.register(ModEntities.HAZMAT_GUARD.get(), context -> new MutantHumanoidRenderer<>(context, "mutationcraft:textures/entity/hazmat_guard.png"));
        registrar.register(ModEntities.SCIENTIST.get(), context -> new MutantHumanoidRenderer<>(context, "mutationcraft:textures/entity/scientist.png"));
        geo(registrar, ModEntities.ASSIMILATED_COW.get(), "assimilated_cow", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_WITCH.get(), "assimilated_witch", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_ENDERMAN.get(), "assimilated_enderman", 0.5F);
        geo(registrar, ModEntities.REDUCTOR.get(), "reductor", 0.5F);
        geo(registrar, ModEntities.HAZMAT_HELICOPTER.get(), "hazmat_helicopter", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_WOLF.get(), "assimilated_wolf", 0.5F);
        registrar.register(ModEntities.HAZMAT_LEADER.get(), context -> new MutantHumanoidRenderer<>(context, "mutationcraft:textures/entity/hazmat_leader.png"));
        registrar.register(ModEntities.HAZMAT_FLAMETHROWER.get(), context -> new MutantHumanoidRenderer<>(context, "mutationcraft:textures/entity/hazmat_flamethrower.png"));
        registrar.register(ModEntities.HAZMAT_MEDIC.get(), context -> new MutantHumanoidRenderer<>(context, "mutationcraft:textures/entity/hazmat_medic.png"));
        geo(registrar, ModEntities.ASSIMILATED_FOX.get(), "assimilated_fox", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_BEAR.get(), "assimilated_bear", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_EVOKER.get(), "assimilated_evoker", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_VEX.get(), "assimilated_vex", 0.5F);
        geo(registrar, ModEntities.CARNIVORAE.get(), "carnivorae", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_DONKEY.get(), "assimilated_donkey", 0.5F);
        geo(registrar, ModEntities.FLAYER.get(), "flayer", 0.5F);
        registrar.register(ModEntities.CORROSION_QUEEN.get(), context -> {
            MutantGeoRenderer<CorrosionQueenEntity> renderer = new MutantGeoRenderer<>(context, "corrosion_queen", 0.5F);
            renderer.addRenderLayer(new CorrosionQueenLayer(renderer));
            return renderer;
        });
        geo(registrar, ModEntities.POISONED_SPIKES.get(), "poisoned_spikes", 0.5F);
        geo(registrar, ModEntities.BLOOD_SPIKE.get(), "blood_spike", 0.5F);
        geo(registrar, ModEntities.HUMAN_HERDER.get(), "human_herder", 0.5F);
        geo(registrar, ModEntities.RESENTER.get(), "resenter", 0.5F);
        geo(registrar, ModEntities.ASSIMILATED_CREEPER.get(), "assimilated_creeper", 0.5F);
        geo(registrar, ModEntities.PARASITIC_RAM.get(), "parasitic_ram", 0.6F);
        geo(registrar, ModEntities.PARASITIC_ROLLER.get(), "parasitic_roller", 0.5F);
        geo(registrar, ModEntities.PARASITIC_SHOOTER.get(), "parasitic_shooter", 0.5F);
        geo(registrar, ModEntities.LIGHT_HOOK.get(), "light_hook", 0.5F);
        geo(registrar, ModEntities.MEDIUM_HOOK.get(), "medium_hook", 0.5F);
        geo(registrar, ModEntities.HEAVY_HOOK.get(), "heavy_hook", 0.5F);
    }

    private static <T extends LivingEntity & AnimatedMutant> void geo(RendererRegistrar registrar, EntityType<T> type, String name, float shadow) {
        registrar.register(type, context -> new MutantGeoRenderer<>(context, name, shadow));
    }

    public interface RendererRegistrar {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
    }

    private ModEntityRenderers() {
    }
}
