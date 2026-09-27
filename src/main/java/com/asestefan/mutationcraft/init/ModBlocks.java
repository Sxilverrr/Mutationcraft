package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.block.PutridBlock;
import com.asestefan.mutationcraft.block.PutridVineBlock;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    public static final ModRegistry<Block> REGISTRY = ModRegistry.create(Registries.BLOCK);

    public static final ModRegistry.Entry<Block> PUTRID_BLOCK = REGISTRY.register("putrid_block", () -> new PutridBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.SOUL_SAND).strength(0.7F, 10.0F).requiresCorrectToolForDrops().randomTicks()));
    public static final ModRegistry.Entry<Block> PUTRID_VINE = REGISTRY.register("putrid_vine", () -> new PutridVineBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.WEEPING_VINES).instabreak().noLootTable().noCollission()));

    private ModBlocks() {
    }
}
