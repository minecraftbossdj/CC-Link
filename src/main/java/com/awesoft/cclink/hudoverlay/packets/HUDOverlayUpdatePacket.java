package com.awesoft.cclink.hudoverlay.packets;

import com.awesoft.cclink.hudoverlay.HUDOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class HUDOverlayUpdatePacket {
    public static class Entry {
        public enum ElementType {
            TEXT,
            RIGHTBOUND_TEXT,
            RECTANGLE,
            ITEM
        }

        public enum ActionType {
            ADD_OR_UPDATE,
            REMOVE,
            REMOVE_ALL
        }

        public ElementType elementType;
        public ActionType actionType;
        public String elementID;
        public String text;
        public String itemResource;
        public int x, y, x2, y2, color, transparency;
        public float scale;

        public Entry() {}

        public static Entry text(String id, String text, int x, int y, int color, float scale, boolean rightbound, boolean remove, boolean removeAll) {
            Entry entry = new Entry();
            entry.elementType = rightbound ? ElementType.RIGHTBOUND_TEXT : ElementType.TEXT;
            entry.actionType = removeAll ? ActionType.REMOVE_ALL : (remove ? ActionType.REMOVE : ActionType.ADD_OR_UPDATE);
            entry.elementID = removeAll ? null : id;
            entry.text = text != null ? text : "";
            entry.x = x;
            entry.y = y;
            entry.color = color;
            entry.scale = scale;
            return entry;
        }

        public static Entry item(String id, String item, int x, int y, boolean remove, boolean removeAll) {
            Entry entry = new Entry();
            entry.elementType = ElementType.ITEM;
            entry.actionType = removeAll ? ActionType.REMOVE_ALL : (remove ? ActionType.REMOVE : ActionType.ADD_OR_UPDATE);
            entry.elementID = removeAll ? null : id;
            entry.itemResource = item != null ? item : "";
            entry.x = x;
            entry.y = y;
            return entry;
        }

        public static Entry rect(String id, int x1, int y1, int x2, int y2, int color, int transparency, boolean remove, boolean removeAll) {
            Entry entry = new Entry();
            entry.elementType = ElementType.RECTANGLE;
            entry.actionType = removeAll ? ActionType.REMOVE_ALL : (remove ? ActionType.REMOVE : ActionType.ADD_OR_UPDATE);
            entry.elementID = removeAll ? null : id;
            entry.x = x1;
            entry.y = y1;
            entry.x2 = x2;
            entry.y2 = y2;
            entry.color = color;
            entry.transparency = transparency;
            return entry;
        }
    }

    private final UUID playerUUID;
    private final List<Entry> entries;

    public HUDOverlayUpdatePacket(UUID playerUUID, List<Entry> entries) {
        this.playerUUID = playerUUID;
        this.entries = entries;
    }

    public static void safeWriteUtf(FriendlyByteBuf buf, String value, String label) {
        if (value == null) {
            throw new IllegalArgumentException("tried to write null to UTF for field: " + label);
        }
        buf.writeUtf(value);
    }

    public static void encode(HUDOverlayUpdatePacket pkt, FriendlyByteBuf buf) {
        buf.writeUUID(pkt.playerUUID);
        buf.writeVarInt(pkt.entries.size());
        for (Entry e : pkt.entries) {
            buf.writeEnum(e.elementType);
            buf.writeEnum(e.actionType);

            boolean hasId = e.actionType != Entry.ActionType.REMOVE_ALL;
            buf.writeBoolean(hasId);
            if (hasId) {
                safeWriteUtf(buf,e.elementID != null ? e.elementID : "","elementId");
            }

            switch (e.elementType) {
                case TEXT, RIGHTBOUND_TEXT -> {
                    if (e.actionType == Entry.ActionType.ADD_OR_UPDATE) {
                        safeWriteUtf(buf,e.text,"text");
                        buf.writeInt(e.x);
                        buf.writeInt(e.y);
                        buf.writeInt(e.color);
                        buf.writeFloat(e.scale);
                    }
                }
                case ITEM -> {
                    if (e.actionType == Entry.ActionType.ADD_OR_UPDATE) {
                        safeWriteUtf(buf,e.itemResource,"itemResource");
                        buf.writeInt(e.x);
                        buf.writeInt(e.y);
                    }
                }
                case RECTANGLE -> {
                    if (e.actionType == Entry.ActionType.ADD_OR_UPDATE) {
                        buf.writeInt(e.x);
                        buf.writeInt(e.y);
                        buf.writeInt(e.x2);
                        buf.writeInt(e.y2);
                        buf.writeInt(e.color);
                        buf.writeInt(e.transparency);
                    }
                }
            }
        }
    }

    public static HUDOverlayUpdatePacket decode(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        int size = buf.readVarInt();
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Entry.ElementType type = buf.readEnum(Entry.ElementType.class);
            Entry.ActionType action = buf.readEnum(Entry.ActionType.class);

            boolean hasId = buf.readBoolean();
            String id = hasId ? buf.readUtf() : null;

            Entry e = new Entry();
            e.elementType = type;
            e.actionType = action;
            e.elementID = hasId ? id : null;

            switch (type) {
                case TEXT, RIGHTBOUND_TEXT -> {
                    if (action == Entry.ActionType.ADD_OR_UPDATE) {
                        e.text = buf.readUtf();
                        e.x = buf.readInt();
                        e.y = buf.readInt();
                        e.color = buf.readInt();
                        e.scale = buf.readFloat();
                    }
                }
                case ITEM -> {
                    if (action == Entry.ActionType.ADD_OR_UPDATE) {
                        e.itemResource = buf.readUtf();
                        e.x = buf.readInt();
                        e.y = buf.readInt();
                    }
                }
                case RECTANGLE -> {
                    if (action == Entry.ActionType.ADD_OR_UPDATE) {
                        e.x = buf.readInt();
                        e.y = buf.readInt();
                        e.x2 = buf.readInt();
                        e.y2 = buf.readInt();
                        e.color = buf.readInt();
                        e.transparency = buf.readInt();
                    }
                }
            }

            entries.add(e);
        }

        return new HUDOverlayUpdatePacket(uuid, entries);
    }

    public static void handle(HUDOverlayUpdatePacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            processPacket(pkt);
        });
        ctx.get().setPacketHandled(true);
    }

    public static void processPacket(HUDOverlayUpdatePacket pkt) {
        UUID uuid = pkt.playerUUID;
        for (Entry e : pkt.entries) {
            switch (e.elementType) {
                case TEXT -> {
                    switch (e.actionType) {
                        case ADD_OR_UPDATE ->
                                HUDOverlay.addOrUpdateTextElement(uuid, e.elementID, e.text, e.x, e.y, e.color, e.scale);
                        case REMOVE -> HUDOverlay.removeTextElement(uuid, e.elementID);
                        case REMOVE_ALL -> HUDOverlay.clearTextElementsForPlayer(uuid);
                    }
                }
                case RIGHTBOUND_TEXT -> {
                    switch (e.actionType) {
                        case ADD_OR_UPDATE ->
                                HUDOverlay.addOrUpdateRightboundTextElement(uuid, e.elementID, e.text, e.x, e.y, e.color, e.scale);
                        case REMOVE -> HUDOverlay.removeRightboundTextElement(uuid, e.elementID);
                        case REMOVE_ALL -> HUDOverlay.clearRightboundTextElementsForPlayer(uuid);
                    }
                }
                case ITEM -> {
                    switch (e.actionType) {
                        case ADD_OR_UPDATE ->
                                HUDOverlay.addOrUpdateItemElement(uuid, e.elementID, e.itemResource, e.x, e.y);
                        case REMOVE -> HUDOverlay.removeItemElement(uuid, e.elementID);
                        case REMOVE_ALL -> HUDOverlay.clearItemElementsForPlayer(uuid);
                    }
                }
                case RECTANGLE -> {
                    switch (e.actionType) {
                        case ADD_OR_UPDATE ->
                                HUDOverlay.addOrUpdateRectangleElement(uuid, e.elementID, e.x, e.y, e.x2, e.y2, e.color, e.transparency);
                        case REMOVE -> HUDOverlay.removeRectangleElement(uuid, e.elementID);
                        case REMOVE_ALL -> HUDOverlay.clearRectangleElementsForPlayer(uuid);
                    }
                }
            }
        }
    }

}
