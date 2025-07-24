package com.awesoft.cclink.networking;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.Registration.ItemRegistry;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkBrain;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkHolder;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkServerComputer;
import com.awesoft.cclink.item.LinkCore.LinkBrain;
import com.awesoft.cclink.item.LinkCore.LinkCoreComputerItem;
import com.awesoft.cclink.item.LinkCore.LinkHolder;
import com.awesoft.cclink.item.LinkCore.LinkServerComputer;
import com.awesoft.cclink.libs.ComputerLib;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.util.InventoryUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static com.awesoft.cclink.libs.ComputerLib.*;

public class OpenLinkPacket {
    public static void encode(OpenLinkPacket msg, FriendlyByteBuf buf) {}

    public static OpenLinkPacket decode(FriendlyByteBuf buf) {
        return new OpenLinkPacket();
    }

    public static void handle(OpenLinkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender().level().isClientSide) {
                CCLink.LOGGER.info("somehow recieved packet on client???????? wtf how??? gang are you good wtf did you do to minecraft");
                return;
            }
            AtomicBoolean isLinkPresent = new AtomicBoolean(false);
            AtomicReference<ItemStack> linkItem = new AtomicReference<ItemStack>(new ItemStack(Items.AIR));
            ServerPlayer player = ctx.get().getSender();
            CuriosApi.getCuriosInventory(player).ifPresent(curiosInv ->{
                Optional<SlotResult> linkCore = curiosInv.findFirstCurio(ItemRegistry.LINK_CORE.get());
                Optional<SlotResult> linkCoreCreative = curiosInv.findFirstCurio(ItemRegistry.LINK_CORE_COMMAND.get());
                Optional<SlotResult> integratedLinkCore = curiosInv.findFirstCurio(ItemRegistry.INTEGRATED_LINK_CORE.get());

                if (linkCore.isPresent()) {
                    isLinkPresent.set(true);
                    linkItem.set(linkCore.get().stack());
                } else if (linkCoreCreative.isPresent()) {
                    isLinkPresent.set(true);
                    linkItem.set(linkCoreCreative.get().stack());
                } else if (integratedLinkCore.isPresent()) {
                    isLinkPresent.set(true);
                    linkItem.set(integratedLinkCore.get().stack());
                }

                if (isLinkPresent.get()) {
                    CuriosApi.getCuriosHelper().findFirstCurio(player, ItemRegistry.LINK_INTERFACE.get()).ifPresent((slotResult) -> {
                        if (linkItem.get().getItem() == ItemRegistry.LINK_CORE.get()) {
                            //CCLink.LOGGER.info("default link");
                            LinkHolder.PlayerCuriosHolder holder = new LinkHolder.PlayerCuriosHolder(player);
                            LinkBrain brain = ComputerLib.getOrCreateBrain((ServerLevel) player.level(), holder, linkItem.get(), ComputerFamily.ADVANCED);
                            LinkServerComputer computer = brain.computer();
                            computer.turnOn();
                            boolean stop = false;
                            IPocketUpgrade upgrade = getUpgrade(linkItem.get());
                            if (upgrade != null) {
                                stop = upgrade.onRightClick(player.level(), brain, computer.getPeripheral(ComputerSide.BACK));
                                ComputerLib.updateItem(linkItem.get(), brain);
                            }
                            if (!stop) {
                                openLinkImpl(player, linkItem.get(), holder, false, computer, true);
                            }
                        } else if (linkItem.get().getItem() == ItemRegistry.LINK_CORE_COMMAND.get()) {
                            //CCLink.LOGGER.info("command link");
                            LinkHolder.PlayerCuriosHolder holder = new LinkHolder.PlayerCuriosHolder(player);
                            LinkBrain brain = ComputerLib.getOrCreateBrain((ServerLevel) player.level(), holder, linkItem.get(), ComputerFamily.COMMAND);
                            LinkServerComputer computer = brain.computer();
                            computer.turnOn();
                            boolean stop = false;
                            IPocketUpgrade upgrade = getUpgrade(linkItem.get());
                            if (upgrade != null) {
                                stop = upgrade.onRightClick(player.level(), brain, computer.getPeripheral(ComputerSide.BACK));
                                ComputerLib.updateItem(linkItem.get(), brain);
                            }
                            if (!stop) {
                                openLinkImpl(player, linkItem.get(), holder, false, computer, false);
                            }
                        } else if (linkItem.get().getItem() == ItemRegistry.INTEGRATED_LINK_CORE.get()) {
                            //CCLink.LOGGER.info("integrated link");
                            IntegratedLinkHolder.PlayerCuriosHolder holder = new IntegratedLinkHolder.PlayerCuriosHolder(player);
                            IntegratedLinkBrain brain = ComputerLib.getOrCreateIntegratedBrain((ServerLevel) player.level(), holder, linkItem.get(), ComputerFamily.ADVANCED);
                            IntegratedLinkServerComputer computer = brain.computer();
                            computer.turnOn();
                            boolean stop = false;
                            IPocketUpgrade upgrade = getUpgrade(linkItem.get());
                            if (upgrade != null) {
                                stop = upgrade.onRightClick(player.level(), brain, computer.getPeripheral(ComputerSide.BACK));
                                ComputerLib.updateIntegratedItem(linkItem.get(), brain);
                            }
                            if (!stop) {
                                openIntegratedLinkImpl(player, linkItem.get(), holder, false, computer, true);
                            }
                        }
                    });
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
