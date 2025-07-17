package com.awesoft.cclink.hudoverlay.packets;


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
}
