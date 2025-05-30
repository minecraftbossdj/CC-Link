package com.awesoft.cclink.hudoverlay.packets.removehud;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HUDRemoveAllRightboundStringPacket {
    private final UUID playerUUID;

    public HUDRemoveAllRightboundStringPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    // Serialization method
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);
    }

    // Deserialization method
    public HUDRemoveAllRightboundStringPacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
    }

    // Handler method
    public static void handle(HUDRemoveAllRightboundStringPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            UUID playerUUID = packet.playerUUID;

            // Handle the removal on the client side
            HUDOverlay.clearRightboundTextElementsForPlayer(playerUUID);
        });
        context.get().setPacketHandled(true);
    }
}
