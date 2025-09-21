package com.awesoft.cclink.events;

import com.awesoft.cclink.Registration.ItemRegistry;
import com.awesoft.cclink.libs.ComputerLib;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Set;

@Mod.EventBusSubscriber
public class ChatEvent {

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {

        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();

        Set<Item> items = Set.of(ItemRegistry.LINK_CORE.get(),ItemRegistry.LINK_CORE_COMMAND.get(),ItemRegistry.INTEGRATED_LINK_CORE.get());
        if (player.getInventory().hasAnyOf(items)) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() == ItemRegistry.LINK_CORE.get().asItem() || player.getInventory().getItem(i).getItem() == ItemRegistry.LINK_CORE_COMMAND.get().asItem()) {
                    ItemStack item = player.getInventory().getItem(i);
                    ServerComputer computer = ComputerLib.getServerComputer(event.getPlayer().getServer(), item);
                    if (computer != null) {
                        Container itemInv = ComputerLib.getContainerFromItem(item);
                        if (itemInv != null && ComputerLib.hasUpgrade("cclink:chatty_upgrade",item)) {
                            Object[] args = new Object[2];
                            args[0] = player.getGameProfile().getName();
                            args[1] = message;
                            computer.queueEvent("chat_message",args);
                        }
                    }
                } else if (player.getInventory().getItem(i).getItem() == ItemRegistry.INTEGRATED_LINK_CORE.get().asItem()) {
                    ItemStack item = player.getInventory().getItem(i);
                    ServerComputer computer = ComputerLib.getIntegratedServerComputer(ServerContext.get(event.getPlayer().server).registry(), item);
                    if (computer != null) {
                        Container itemInv = ComputerLib.getContainerFromItem(item);
                        if (itemInv != null && ComputerLib.hasUpgrade("cclink:chatty_upgrade",item)) {
                            Object[] args = new Object[2];
                            args[0] = player.getGameProfile().getName();
                            args[1] = message;
                            computer.queueEvent("chat_message",args);
                        }
                    }
                }
            }
        }

        CuriosApi.getCuriosHelper().findFirstCurio(player, ItemRegistry.LINK_CORE.get()).ifPresent((slotResult) -> {
            ItemStack item = slotResult.stack();
            ServerComputer computer = ComputerLib.getServerComputer(event.getPlayer().getServer(), item);
            if (computer != null) {
                Container itemInv = ComputerLib.getContainerFromItem(item);
                if (itemInv != null && ComputerLib.hasUpgrade("cclink:chatty_upgrade",item)) {
                    Object[] args = new Object[2];
                    args[0] = player.getGameProfile().getName();
                    args[1] = message;
                    computer.queueEvent("chat_message",args);
                }
            }
        });

        CuriosApi.getCuriosHelper().findFirstCurio(player, ItemRegistry.LINK_CORE_COMMAND.get()).ifPresent((slotResult) -> {
            ItemStack item = slotResult.stack();
            ServerComputer computer = ComputerLib.getServerComputer(event.getPlayer().getServer(), item);
            if (computer != null) {
                Container itemInv = ComputerLib.getContainerFromItem(item);
                if (itemInv != null && ComputerLib.hasUpgrade("cclink:chatty_upgrade",item)) {
                    Object[] args = new Object[2];
                    args[0] = player.getGameProfile().getName();
                    args[1] = message;
                    computer.queueEvent("chat_message",args);
                }
            }
        });

        CuriosApi.getCuriosHelper().findFirstCurio(player, ItemRegistry.INTEGRATED_LINK_CORE.get()).ifPresent((slotResult) -> {
            ItemStack item = slotResult.stack();
            ServerComputer computer = ComputerLib.getIntegratedServerComputer(ServerContext.get(event.getPlayer().server).registry(), item);
            if (computer != null) {
                Container itemInv = ComputerLib.getContainerFromItem(item);
                if (itemInv != null && ComputerLib.hasUpgrade("cclink:chatty_upgrade",item)) {
                    Object[] args = new Object[2];
                    args[0] = player.getGameProfile().getName();
                    args[1] = message;
                    computer.queueEvent("chat_message",args);
                }
            }
        });
    }
}
