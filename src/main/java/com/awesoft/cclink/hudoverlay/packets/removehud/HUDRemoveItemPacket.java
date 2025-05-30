package com.awesoft.cclink.hudoverlay.packets.removehud;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HUDRemoveItemPacket {
    private final UUID playerUUID;
    private final String key;

    public HUDRemoveItemPacket(UUID playerUUID, String key) {
        this.playerUUID = playerUUID;
        this.key = key;
    }

    // Serialization method
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);
        buffer.writeUtf(key);
    }

    // Deserialization method
    public HUDRemoveItemPacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
        this.key = buffer.readUtf();
    }

    // Handler method
    public static void handle(HUDRemoveItemPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            UUID playerUUID = packet.playerUUID;
            String key = packet.key;

            // Handle the removal on the client side
            HUDOverlay.removeItemElement(playerUUID, key);
        });
        context.get().setPacketHandled(true);
    }
}

