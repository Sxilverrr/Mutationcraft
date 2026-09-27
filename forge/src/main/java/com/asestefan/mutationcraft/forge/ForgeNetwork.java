package com.asestefan.mutationcraft.forge;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import com.asestefan.mutationcraft.network.ModVariables;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ForgeNetwork {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ModUtil.id(MutationcraftMod.MODID, MutationcraftMod.MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init() {
        CHANNEL.registerMessage(0, SyncMessage.class, SyncMessage::encode, SyncMessage::decode, SyncMessage::handle);
    }

    public static void sendToPlayer(ServerPlayer player, int type, CompoundTag data) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncMessage(type, data));
    }

    public record SyncMessage(int type, CompoundTag data) {
        public static void encode(SyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.type);
            buffer.writeNbt(message.data);
        }

        public static SyncMessage decode(FriendlyByteBuf buffer) {
            return new SyncMessage(buffer.readInt(), buffer.readNbt());
        }

        public static void handle(SyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer()) {
                    ModVariables.handleSync(message.type, message.data);
                }
            });
            context.setPacketHandled(true);
        }
    }

    private ForgeNetwork() {
    }
}
