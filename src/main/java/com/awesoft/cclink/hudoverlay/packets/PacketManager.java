package com.awesoft.cclink.hudoverlay.packets;


import com.awesoft.cclink.networking.OverlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketManager {

    public static void sendToClient(UUID playerUUID, HUDOverlayUpdatePacket packet) {
        if (!ServerLifecycleHooks.getCurrentServer().isDedicatedServer()) {return;}
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);

        if (player == null) {
            System.err.println("player actually is cooked");
            return;
        }


        OverlayNetworkHandler.OVERLAY_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
