package com.awesoft.cclink.hudoverlay.packets.removehud;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveRightboundStringHUDPacket {
    private final UUID playerUUID;
    private final String elementID;

    public RemoveRightboundStringHUDPacket(UUID playerUUID, String elementID) {
        this.playerUUID = playerUUID;
        this.elementID = elementID;
    }

    public RemoveRightboundStringHUDPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.elementID = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(elementID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Remove the rightbound string element from the HUD
            HUDOverlay.removeRightboundTextElement(playerUUID, elementID);
        });
        return true;
    }
}