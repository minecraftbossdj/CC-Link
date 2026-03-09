package com.awesoft.cclink;

import com.awesoft.cclink.registration.Keybinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cclink", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCLinkClient {
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.OPEN_LINK);

        CCLink.LOGGER.info("oh shit is that a keybind??? EVERYONE SCRAM!!!!");
    }


}
