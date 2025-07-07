package com.awesoft.cclink;

import com.awesoft.cclink.Registration.APIRegistry;
import com.awesoft.cclink.Registration.ItemRegistry;
import com.awesoft.cclink.Registration.TabInit;
import com.awesoft.cclink.networking.OverlayNetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cclink")
public class CCLink {

    public static final String NAME = "CC Link";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    // Our mod id


    public static final String MODID = "cclink";




    public CCLink() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info("CC Link says helor! :3");
        //Registration.register();
        TabInit.CREATIVE_MODE_TABS.register(bus);
        ItemRegistry.register(bus);
        LOGGER.info("registry says haiiiii!!!! :3");
        OverlayNetworkHandler.registerOverlayPackets();
        LOGGER.info("packets say \"whats up fella\" (hes a lil weird)");
        APIRegistry.register();
        //EventRegister.Register();
        CCLinkConfig.register(ModLoadingContext.get());
        LOGGER.info("heloer!! -from config! :3");
        // Register ourselves for server and other game events we are interested in. Currently, we do not use any events
        MinecraftForge.EVENT_BUS.register(this);

        bus.addListener(this::onClientSetup);

    }

    private void onClientSetup(final FMLClientSetupEvent event) {

    }
}
