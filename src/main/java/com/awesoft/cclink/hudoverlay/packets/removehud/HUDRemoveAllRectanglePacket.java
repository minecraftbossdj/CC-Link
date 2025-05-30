package com.awesoft.cclink.hudoverlay.packets.removehud;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HUDRemoveAllRectanglePacket {
    private final UUID playerUUID;

    public HUDRemoveAllRectanglePacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    // Serialization method
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);
    }

    // Deserialization method
    public HUDRemoveAllRectanglePacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
    }

    // Handler method
    public static void handle(HUDRemoveAllRectanglePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            UUID playerUUID = packet.playerUUID;

            // Handle the removal on the client side
            HUDOverlay.clearRectangleElementsForPlayer(playerUUID);
        });
        context.get().setPacketHandled(true);
    }
}

