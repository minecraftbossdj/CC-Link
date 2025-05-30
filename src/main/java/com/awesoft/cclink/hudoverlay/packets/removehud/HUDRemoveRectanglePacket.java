package com.awesoft.cclink.hudoverlay.packets.removehud;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HUDRemoveRectanglePacket {
    private final UUID playerUUID;
    private final String key;

    public HUDRemoveRectanglePacket(UUID playerUUID, String key) {
        this.playerUUID = playerUUID;
        this.key = key;
    }

    // Serialization method
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);
        buffer.writeUtf(key);
    }

    // Deserialization method
    public HUDRemoveRectanglePacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
        this.key = buffer.readUtf();
    }

    // Handler method
    public static void handle(HUDRemoveRectanglePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            UUID playerUUID = packet.playerUUID;
            String key = packet.key;

            // Handle the removal on the client side
            HUDOverlay.removeRectangleElement(playerUUID, key);
        });
        context.get().setPacketHandled(true);
    }
}
