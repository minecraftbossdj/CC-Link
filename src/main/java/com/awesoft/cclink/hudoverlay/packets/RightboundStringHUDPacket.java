package com.awesoft.cclink.hudoverlay.packets;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RightboundStringHUDPacket {
    private final UUID playerUUID;
    private final String elementID;
    private final String text;
    private final int x, y;
    private final int color;
    private final float scale;

    public RightboundStringHUDPacket(UUID playerUUID, String elementID, String text, int x, int y, int color, float scale) {
        this.playerUUID = playerUUID;
        this.elementID = elementID;
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.scale = scale;
    }

    public RightboundStringHUDPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.elementID = buf.readUtf();
        this.text = buf.readUtf();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.color = buf.readInt();
        this.scale = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(elementID);
        buf.writeUtf(text);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(color);
        buf.writeFloat(scale);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Add or update the rightbound string element on the HUD
            HUDOverlay.addOrUpdateRightboundTextElement(playerUUID, elementID, text, x, y, color, scale);
        });
        return true;
    }
}
