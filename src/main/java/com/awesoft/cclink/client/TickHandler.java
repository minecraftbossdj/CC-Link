package com.awesoft.cclink.client;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.Registration.ItemRegistry;
import com.awesoft.cclink.Registration.Keybinds;
import com.awesoft.cclink.networking.OpenLinkNetworkHandler;
import com.awesoft.cclink.networking.OpenLinkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = "cclink", value = Dist.CLIENT)
public class TickHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END || Minecraft.getInstance().player == null) return;

        LocalPlayer plr = Minecraft.getInstance().player;
        AtomicBoolean isLinkPresent = new AtomicBoolean(false);

        while (Keybinds.OPEN_LINK.consumeClick()) {
            //my brain is telling me this is going to explode, but ig ill find out
            CuriosApi.getCuriosInventory(plr).ifPresent(curiosInv ->{
                Optional<SlotResult> linkCore = curiosInv.findFirstCurio(ItemRegistry.LINK_CORE.get());
                Optional<SlotResult> linkCoreCreative = curiosInv.findFirstCurio(ItemRegistry.LINK_CORE_COMMAND.get());
                Optional<SlotResult> integratedLinkCore = curiosInv.findFirstCurio(ItemRegistry.INTEGRATED_LINK_CORE.get());

                if (linkCore.isPresent()) {
                    isLinkPresent.set(true);
                } else if (linkCoreCreative.isPresent()) {
                    isLinkPresent.set(true);
                } else if (integratedLinkCore.isPresent()) {
                    isLinkPresent.set(true);
                }

                if (isLinkPresent.get()) {
                    OpenLinkNetworkHandler.OPENLINK_CHANNEL.sendToServer(new OpenLinkPacket());
                }
            });


        }
    }
}
