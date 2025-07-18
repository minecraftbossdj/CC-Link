package com.awesoft.cclink.networking;

import com.awesoft.cclink.hudoverlay.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

// Define a new channel for overlay packets
public class OverlayNetworkHandler {
    private static final String OVERLAY_PROTOCOL_VERSION = "1";
    public static final SimpleChannel OVERLAY_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("cclink", "overlay_network"),
            () -> OVERLAY_PROTOCOL_VERSION,
            OVERLAY_PROTOCOL_VERSION::equals,
            OVERLAY_PROTOCOL_VERSION::equals
    );

    // Register packets for the overlay channel
    public static void registerOverlayPackets() {
        int id = 0;
        OVERLAY_CHANNEL.messageBuilder(HUDOverlayUpdatePacket.class, id++)
                .encoder(HUDOverlayUpdatePacket::encode)
                .decoder(HUDOverlayUpdatePacket::decode)
                .consumerMainThread(HUDOverlayUpdatePacket::handle)
                .add();

    }

}

