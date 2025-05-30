package com.awesoft.cclink.hudoverlay.packets;


import com.awesoft.cclink.hudoverlay.packets.removehud.*;
import com.awesoft.cclink.networking.OverlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketManager {

    public static void sendToClient(UUID playerUUID, Object packet) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            OverlayNetworkHandler.OVERLAY_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }
    //add or update

    public static void sendRightboundTextPacketToPlayer(UUID playerUUID, String key, String text, int x, int y, int color, float scale) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            RightboundStringHUDPacket packet = new RightboundStringHUDPacket(playerUUID, key, text, x, y, color, scale);
            sendToClient(playerUUID,packet);
        }
    }

    public static void sendTextPacketToPlayer(UUID playerUUID, String key, String text, int x, int y, int color, float scale) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            HUDTextPacket packet = new HUDTextPacket(playerUUID, key, text, x, y, color, scale);
            sendToClient(playerUUID,packet);
        }
    }

    public static void sendItemPacketToPlayer(UUID playerUUID, String key, String text, int x, int y) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            HUDItemPacket packet = new HUDItemPacket(playerUUID, key, text, x, y);
            sendToClient(playerUUID,packet);
        }
    }

    public static void sendRectPacketToPlayer(UUID playerUUID, String key, int x1, int y1,int x2, int y2, int color, int transparency) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            HUDRectanglePacket packet = new HUDRectanglePacket(playerUUID, key, x1, y1,x2,y2,color,transparency);
            sendToClient(playerUUID,packet);
        }
    }
    //remove
    public static void removeRightboundTextElementPacket(UUID playerUUID, String key) {
        RemoveRightboundStringHUDPacket packet = new RemoveRightboundStringHUDPacket(playerUUID, key);
        sendToClient(playerUUID, packet);
    }

    public static void removeTextElementPacket(UUID playerUUID, String key) {
        HUDRemoveTextPacket packet = new HUDRemoveTextPacket(playerUUID, key);
        sendToClient(playerUUID, packet);
    }

    public static void removeItemElementPacket(UUID playerUUID, String key) {
        HUDRemoveItemPacket packet = new HUDRemoveItemPacket(playerUUID, key);
        sendToClient(playerUUID, packet);
    }

    public static void removeRectangleElementPacket(UUID playerUUID, String key) {
        HUDRemoveRectanglePacket packet = new HUDRemoveRectanglePacket(playerUUID, key);
        sendToClient(playerUUID, packet);
    }
    //remove all
    public static void removeAllRightboundTextElementsPacket(UUID playerUUID) {
        HUDRemoveAllRightboundStringPacket packet = new HUDRemoveAllRightboundStringPacket(playerUUID);
        sendToClient(playerUUID, packet);
    }

    public static void removeAllTextElementsPacket(UUID playerUUID) {
        HUDRemoveAllTextPacket packet = new HUDRemoveAllTextPacket(playerUUID);
       sendToClient(playerUUID, packet);
    }

    public static void removeAllItemElementsPacket(UUID playerUUID) {
        HUDRemoveAllItemPacket packet = new HUDRemoveAllItemPacket(playerUUID);
       sendToClient(playerUUID, packet);
    }

    public static void removeAllRectangleElementsPacket(UUID playerUUID) {
        HUDRemoveAllRectanglePacket packet = new HUDRemoveAllRectanglePacket(playerUUID);
       sendToClient(playerUUID, packet);
    }
    
}
