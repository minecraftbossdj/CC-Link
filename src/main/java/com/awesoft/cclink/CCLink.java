package com.awesoft.cclink;

import com.awesoft.cclink.registration.*;
import com.awesoft.cclink.client.LinkTurtleRenderer;
import com.awesoft.cclink.datagen.recipes.ModRecipes;
import com.awesoft.cclink.gui.armor.ArmorScreen;
import com.awesoft.cclink.gui.client.LinkScreen;
import com.awesoft.cclink.gui.integrated.IntegratedLinkScreen;
import com.awesoft.cclink.networking.OpenLinkNetworkHandler;
import com.awesoft.cclink.networking.OverlayNetworkHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("cclink")
public class CCLink {

    public static final String NAME = "CC Link";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final String MODID = "cclink";

    public CCLink() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);

        LOGGER.info("CC Link says helor! :3");
        TabInit.CREATIVE_MODE_TABS.register(bus);
        ItemRegistry.register(bus);
        BlockRegistry.register(bus);
        LOGGER.info("registry says haiiiii!!!! :3");
        APIRegistry.register();
        LOGGER.info("AHHHH CC APIS AHHHHHH IM SCARED AHHHHHHH!!!!!");
        ModRecipes.register(bus);
        LOGGER.info("RAHHHHH MOD RECIPES JUMPSCARE! did i scare you? I'm sure scared, ugh I dont wanna touch datagen ever again :sob:");
        CCLinkConfig.register(ModLoadingContext.get());
        LOGGER.info("heloer!! -from config! :3");
        CCMenus.registerMenu(bus);
        LOGGER.info("uh oh that MIGHT be the menus ngl");
        PocketRegistry.POCKET_SERIALIZER.register(bus);
        LOGGER.info("yoo pocket upgrades? hell yeah");


        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onCommonSetup(FMLCommonSetupEvent evt) {
        OverlayNetworkHandler.registerOverlayPackets();
        OpenLinkNetworkHandler.registerOpenLinkPackets();
        LOGGER.info("packets say \"whats up fella\" (hes a lil weird)");
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
            BlockRegistry.LINK_TURTLE_ADVANCED_ENTITY.get(),
            LinkTurtleRenderer::new
        );

        MenuScreens.register(CCMenus.LINK_MENU.get(), LinkScreen::new);
        MenuScreens.register(CCMenus.INTEGRATED_LINK_MENU.get(), IntegratedLinkScreen::new);

        MenuScreens.register(CCMenus.ARMOR_MENU.get(), ArmorScreen::new);
        LOGGER.info("client startup trolling complete, returning to HQ");
    }

}
