package com.awesoft.cclink.hudoverlay.packets;


import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.networking.OverlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketManager {

    public static void sendToClient(UUID playerUUID, HUDOverlayUpdatePacket packet) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);

        if (player == null) {
            System.err.println("player actually is cooked");
            return;
        }

        if (player.level().isClientSide) {
            CCLink.LOGGER.info("casually running clientside, dont mind me"); //this is probably a giant issue, im ngl, but im going to ignore it because i dont feel like fixing it :sob:
            HUDOverlayUpdatePacket.processPacket(packet);
            return;
        }

        OverlayNetworkHandler.OVERLAY_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
