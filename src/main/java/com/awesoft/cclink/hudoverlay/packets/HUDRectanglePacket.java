package com.awesoft.cclink.hudoverlay.packets;

// HUDRectanglePacket.java
import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HUDRectanglePacket {
    private final UUID playerUUID;
    private final String elementID;
    private final int x1, y1, x2, y2;
    private final int color;        // RGB color value
    private final int transparency; // Transparency as an integer (0-100)

    public HUDRectanglePacket(UUID playerUUID, String elementID, int x1, int y1, int x2, int y2, int color, int transparency) {
        this.playerUUID = playerUUID;
        this.elementID = elementID;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.transparency = transparency;
    }

    public HUDRectanglePacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.elementID = buf.readUtf();
        this.x1 = buf.readInt();
        this.y1 = buf.readInt();
        this.x2 = buf.readInt();
        this.y2 = buf.readInt();
        this.color = buf.readInt();
        this.transparency = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(elementID);
        buf.writeInt(x1);
        buf.writeInt(y1);
        buf.writeInt(x2);
        buf.writeInt(y2);
        buf.writeInt(color);
        buf.writeInt(transparency);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Pass color and transparency directly to HUDOverlay
            HUDOverlay.addOrUpdateRectangleElement(playerUUID, elementID, x1, y1, x2, y2, color, transparency);
        });
        return true;
    }

    public UUID getPlayerUUID() { return playerUUID; }
    public String getElementID() { return elementID; }
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public int getColor() { return color; }
    public int getTransparency() { return transparency; }
}

