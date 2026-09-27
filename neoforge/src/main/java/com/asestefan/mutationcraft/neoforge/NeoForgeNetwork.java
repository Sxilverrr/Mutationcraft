package com.asestefan.mutationcraft.neoforge;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.network.ModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class NeoForgeNetwork {
    static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(SyncPayload.TYPE, SyncPayload.CODEC, NeoForgeNetwork::handle);
    }

    private static void handle(SyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ModVariables.handleSync(payload.kind(), payload.data()));
    }

    public static void sendToPlayer(ServerPlayer player, int type, CompoundTag data) {
        PacketDistributor.sendToPlayer(player, new SyncPayload(type, data));
    }

    public record SyncPayload(int kind, CompoundTag data) implements CustomPacketPayload {
        public static final Type<SyncPayload> TYPE = new Type<>(ModUtil.id(MutationcraftMod.MODID, "variables"));
        public static final StreamCodec<FriendlyByteBuf, SyncPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, SyncPayload::kind,
                ByteBufCodecs.COMPOUND_TAG, SyncPayload::data,
                SyncPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private NeoForgeNetwork() {
    }
}
