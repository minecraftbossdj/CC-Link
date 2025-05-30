package com.awesoft.cclink.networking;

import com.awesoft.cclink.hudoverlay.packets.HUDItemPacket;
import com.awesoft.cclink.hudoverlay.packets.HUDRectanglePacket;
import com.awesoft.cclink.hudoverlay.packets.HUDTextPacket;
import com.awesoft.cclink.hudoverlay.packets.RightboundStringHUDPacket;
import com.awesoft.cclink.hudoverlay.packets.removehud.*;
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
            new ResourceLocation("cclink", "overlay"),
            () -> OVERLAY_PROTOCOL_VERSION,
            OVERLAY_PROTOCOL_VERSION::equals,
            OVERLAY_PROTOCOL_VERSION::equals
    );

    // Register packets for the overlay channel
    public static void registerOverlayPackets() {
        int id = 0;

        OVERLAY_CHANNEL.messageBuilder(HUDTextPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDTextPacket::toBytes)
                .decoder(HUDTextPacket::new)
                .consumerMainThread(HUDTextPacket::handle)
                .add();

        OVERLAY_CHANNEL.messageBuilder(HUDItemPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDItemPacket::toBytes)
                .decoder(HUDItemPacket::new)
                .consumerMainThread(HUDItemPacket::handle)
                .add();

        OVERLAY_CHANNEL.messageBuilder(HUDRectanglePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRectanglePacket::toBytes)
                .decoder(HUDRectanglePacket::new)
                .consumerMainThread(HUDRectanglePacket::handle)
                .add();

        OVERLAY_CHANNEL.messageBuilder(HUDRemoveTextPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveTextPacket::toBytes)
                .decoder(HUDRemoveTextPacket::new)
                .consumerMainThread(HUDRemoveTextPacket::handle)
                .add();
        OVERLAY_CHANNEL.messageBuilder(HUDRemoveItemPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveItemPacket::toBytes)
                .decoder(HUDRemoveItemPacket::new)
                .consumerMainThread(HUDRemoveItemPacket::handle)
                .add();
        OVERLAY_CHANNEL.messageBuilder(HUDRemoveRectanglePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveRectanglePacket::toBytes)
                .decoder(HUDRemoveRectanglePacket::new)
                .consumerMainThread(HUDRemoveRectanglePacket::handle)
                .add();
        //remove all
        OVERLAY_CHANNEL.messageBuilder(HUDRemoveAllTextPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveAllTextPacket::toBytes)
                .decoder(HUDRemoveAllTextPacket::new)
                .consumerMainThread(HUDRemoveAllTextPacket::handle)
                .add();
        OVERLAY_CHANNEL.messageBuilder(HUDRemoveAllItemPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveAllItemPacket::toBytes)
                .decoder(HUDRemoveAllItemPacket::new)
                .consumerMainThread(HUDRemoveAllItemPacket::handle)
                .add();
        OVERLAY_CHANNEL.messageBuilder(HUDRemoveAllRectanglePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveAllRectanglePacket::toBytes)
                .decoder(HUDRemoveAllRectanglePacket::new)
                .consumerMainThread(HUDRemoveAllRectanglePacket::handle)
                .add();
        OVERLAY_CHANNEL.messageBuilder(RightboundStringHUDPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(RightboundStringHUDPacket::toBytes)
                .decoder(RightboundStringHUDPacket::new)
                .consumerMainThread(RightboundStringHUDPacket::handle)
                .add();

        OVERLAY_CHANNEL.messageBuilder(RemoveRightboundStringHUDPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(RemoveRightboundStringHUDPacket::toBytes)
                .decoder(RemoveRightboundStringHUDPacket::new)
                .consumerMainThread(RemoveRightboundStringHUDPacket::handle)
                .add();
        OVERLAY_CHANNEL.messageBuilder(HUDRemoveAllRightboundStringPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HUDRemoveAllRightboundStringPacket::toBytes)
                .decoder(HUDRemoveAllRightboundStringPacket::new)
                .consumerMainThread(HUDRemoveAllRightboundStringPacket::handle)
                .add();
    }

    // Helper method to send a packet to a specific player via the overlay channel
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        OVERLAY_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    // Helper method to send a packet to all players via the overlay channel
    public static void sendToAll(Object packet) {
        OVERLAY_CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }
}

