package com.awesoft.cclink.hudoverlay.packets;

// HUDItemPacket.java
import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HUDItemPacket {
    private final UUID playerUUID;
    private final String elementID;
    private final String itemResource;
    private final int x, y;

    public HUDItemPacket(UUID playerUUID, String elementID, String itemResource, int x, int y) {
        this.playerUUID = playerUUID;
        this.elementID = elementID;
        this.itemResource = itemResource;
        this.x = x;
        this.y = y;
    }

    public HUDItemPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.elementID = buf.readUtf();
        this.itemResource = buf.readUtf();
        this.x = buf.readInt();
        this.y = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(elementID);
        buf.writeUtf(itemResource);
        buf.writeInt(x);
        buf.writeInt(y);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Use HUDOverlay to handle the item rendering
            HUDOverlay.addOrUpdateItemElement(playerUUID, elementID, itemResource, x, y);
        });
        return true;
    }

    public UUID getPlayerUUID() { return playerUUID; }
    public String getElementID() { return elementID; }
    public String getItemResource() { return itemResource; }
    public int getX() { return x; }
    public int getY() { return y; }
}

