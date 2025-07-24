package com.awesoft.cclink.networking;

import com.awesoft.cclink.hudoverlay.packets.HUDOverlayUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

// Define a new channel for overlay packets
public class OpenLinkNetworkHandler {
    private static final String OPENLINK_PROTOCOL_VERSION = "1";
    public static final SimpleChannel OPENLINK_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("cclink", "open_link_network"),
            () -> OPENLINK_PROTOCOL_VERSION,
            OPENLINK_PROTOCOL_VERSION::equals,
            OPENLINK_PROTOCOL_VERSION::equals
    );

    // Register packets for the overlay channel
    public static void registerOpenLinkPackets() {
        int id = 0;
        OPENLINK_CHANNEL.registerMessage(id++, OpenLinkPacket.class,
                OpenLinkPacket::encode,
                OpenLinkPacket::decode,
                OpenLinkPacket::handle
        );
    }

}

