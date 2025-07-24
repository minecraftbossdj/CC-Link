package com.awesoft.cclink;

import com.awesoft.cclink.Registration.ItemRegistry;
import com.awesoft.cclink.Registration.Keybinds;
import com.awesoft.cclink.networking.OpenLinkNetworkHandler;
import com.awesoft.cclink.networking.OpenLinkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = "cclink", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCLinkClient {
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.OPEN_LINK);

        CCLink.LOGGER.info("oh shit is that a keybind??? EVERYONE SCRAM!!!!");
    }


}
