package com.awesoft.cclink;

import com.awesoft.cclink.Registration.*;
import com.awesoft.cclink.client.LinkTurtleRenderer;
import com.awesoft.cclink.datagen.recipes.ModRecipes;
import com.awesoft.cclink.networking.OpenLinkNetworkHandler;
import com.awesoft.cclink.networking.OpenLinkPacket;
import com.awesoft.cclink.networking.OverlayNetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cclink")
public class CCLink {

    public static final String NAME = "CC Link";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    // Our mod id


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
        LOGGER.info("RAHHHHH MOD RECIPES JUMPSCARE! did i scare you? i hope i did, mod recipes quite scary fr");
        CCLinkConfig.register(ModLoadingContext.get());
        LOGGER.info("heloer!! -from config! :3");



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
    }

}
