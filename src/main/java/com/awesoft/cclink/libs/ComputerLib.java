package com.awesoft.cclink.libs;

import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkBrain;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkHolder;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkServerComputer;
import com.awesoft.cclink.item.LinkCore.LinkBrain;
import com.awesoft.cclink.item.LinkCore.LinkHolder;
import com.awesoft.cclink.item.LinkCore.LinkServerComputer;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.impl.PocketUpgrades;
import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;
import dan200.computercraft.shared.computer.items.IComputerItem;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;


public class ComputerLib {
    @Nullable
    public static IPocketUpgrade getUpgrade(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        return compound != null && compound.contains("Upgrade") ? (IPocketUpgrade) PocketUpgrades.instance().get(compound.getString("Upgrade")) : null;
    }

    public static void openLinkImpl(Player player, ItemStack stack, LinkHolder holder, boolean isTypingOnly, ServerComputer computer, boolean curios) {
        if (curios) { //this might be a terrible idea ngl
            PlatformHelper.get().openMenu(player, stack.getHoverName(), (id, inventory, entity) -> new ComputerMenuWithoutInventory(isTypingOnly ? (MenuType) ModRegistry.Menus.POCKET_COMPUTER_NO_TERM.get() : (MenuType) ModRegistry.Menus.COMPUTER.get(), id, inventory, (p) -> holder.isCuriosValid(computer,stack), computer), new ComputerContainerData(computer, stack));
        } else {
            PlatformHelper.get().openMenu(player, stack.getHoverName(), (id, inventory, entity) -> new ComputerMenuWithoutInventory(isTypingOnly ? (MenuType) ModRegistry.Menus.POCKET_COMPUTER_NO_TERM.get() : (MenuType) ModRegistry.Menus.COMPUTER.get(), id, inventory, (p) -> holder.isValid(computer), computer), new ComputerContainerData(computer, stack));
        }
    }

    public static void openIntegratedLinkImpl(Player player, ItemStack stack, IntegratedLinkHolder holder, boolean isTypingOnly, ServerComputer computer, boolean curios) {
        if (curios) { //this might be a terrible idea ngl
            PlatformHelper.get().openMenu(player, stack.getHoverName(), (id, inventory, entity) -> new ComputerMenuWithoutInventory(isTypingOnly ? (MenuType) ModRegistry.Menus.POCKET_COMPUTER_NO_TERM.get() : (MenuType) ModRegistry.Menus.COMPUTER.get(), id, inventory, (p) -> holder.isCuriosValid(computer,stack), computer), new ComputerContainerData(computer, stack));
        } else {
            PlatformHelper.get().openMenu(player, stack.getHoverName(), (id, inventory, entity) -> new ComputerMenuWithoutInventory(isTypingOnly ? (MenuType) ModRegistry.Menus.POCKET_COMPUTER_NO_TERM.get() : (MenuType) ModRegistry.Menus.COMPUTER.get(), id, inventory, (p) -> holder.isValid(computer), computer), new ComputerContainerData(computer, stack));
        }
    }



    public static LinkBrain getOrCreateBrain(ServerLevel level, LinkHolder holder, ItemStack stack, ComputerFamily family) {
        ServerComputerRegistry registry = ServerContext.get(level.getServer()).registry();
        LinkServerComputer computer = getServerComputer(registry, stack);
        if (computer != null) {
            LinkBrain brain = computer.getBrain();
            brain.updateHolder(holder);
            return brain;
        } else {
            int computerID = getComputerID(stack);
            if (computerID < 0) {
                computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(level.getServer(), "computer");
                setComputerID(stack, computerID);
            }

            LinkBrain brain = new LinkBrain(holder, getUpgradeWithData(stack), ServerComputer.properties(getComputerID(stack), family).label(getLabel(stack)));
            LinkServerComputer computerSigma = brain.computer();
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("SessionId", registry.getSessionID());
            tag.putUUID("InstanceId", computerSigma.register());
            if (isMarkedOn(stack)) {
                computerSigma.turnOn();
            }

            updateItem(stack, brain);
            holder.setChanged();
            return brain;
        }
    }

    public static boolean updateItem(ItemStack stack, LinkBrain brain) {
        boolean changed = brain.updateItem(stack);
        LinkServerComputer computer = brain.computer();
        int id = computer.getID();
        if (id != getComputerID(stack)) {
            changed = true;
            setComputerID(stack, id);
        }

        String label = computer.getLabel();
        if (!Objects.equals(label, getLabel(stack))) {
            changed = true;
            setLabel(stack, label);
        }

        boolean on = computer.isOn();
        if (on != isMarkedOn(stack)) {
            changed = true;
            stack.getOrCreateTag().putBoolean("On", on);
        }

        return changed;
    }

    public static boolean updateIntegratedItem(ItemStack stack, IntegratedLinkBrain brain) {
        boolean changed = brain.updateItem(stack);
        IntegratedLinkServerComputer computer = brain.computer();
        int id = computer.getID();
        if (id != getComputerID(stack)) {
            changed = true;
            setComputerID(stack, id);
        }

        String label = computer.getLabel();
        if (!Objects.equals(label, getLabel(stack))) {
            changed = true;
            setLabel(stack, label);
        }

        boolean on = computer.isOn();
        if (on != isMarkedOn(stack)) {
            changed = true;
            stack.getOrCreateTag().putBoolean("On", on);
        }

        return changed;
    }

    public static IntegratedLinkBrain getOrCreateIntegratedBrain(ServerLevel level, IntegratedLinkHolder holder, ItemStack stack, ComputerFamily family) {
        ServerComputerRegistry registry = ServerContext.get(level.getServer()).registry();
        IntegratedLinkServerComputer computer = getIntegratedServerComputer(registry, stack);
        if (computer != null) {
            IntegratedLinkBrain brain = computer.getBrain();
            brain.updateHolder(holder);
            return brain;
        } else {
            int computerID = getComputerID(stack);
            if (computerID < 0) {
                computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(level.getServer(), "computer");
                setComputerID(stack, computerID);
            }

            IntegratedLinkBrain brain = new IntegratedLinkBrain(holder, getUpgradeWithData(stack), ServerComputer.properties(getComputerID(stack), family).label(getLabel(stack)));
            IntegratedLinkServerComputer computerSigma = brain.computer();
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("SessionId", registry.getSessionID());
            tag.putUUID("InstanceId", computerSigma.register());
            if (isMarkedOn(stack)) {
                computerSigma.turnOn();
            }

            updateIntegratedItem(stack, brain);
            holder.setChanged();
            return brain;
        }
    }

    public static int getComputerID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("ComputerId") ? nbt.getInt("ComputerId") : -1;
    }

    public static void setComputerID(ItemStack stack, int computerID) {
        stack.getOrCreateTag().putInt("ComputerId", computerID);
    }

    @Nullable
    public static String getLabel(ItemStack stack) {
        return stack.hasCustomHoverName() ? stack.getHoverName().getString() : null;
    }

    public static boolean isMarkedOn(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.getBoolean("On");
    }

    public static boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label != null) {
            stack.setHoverName(Component.literal(label));
        } else {
            stack.resetHoverName();
        }

        return true;
    }

    @Nullable
    public static LinkServerComputer getServerComputer(ServerComputerRegistry registry, ItemStack stack) {
        return (LinkServerComputer)registry.get(getSessionID(stack), getInstanceID(stack));
    }

    @Nullable
    public static IntegratedLinkServerComputer getIntegratedServerComputer(ServerComputerRegistry registry, ItemStack stack) {
        return (IntegratedLinkServerComputer)registry.get(getSessionID(stack), getInstanceID(stack));
    }

    @Nullable
    public static LinkServerComputer getServerComputer(MinecraftServer server, ItemStack stack) {
        return getServerComputer(ServerContext.get(server).registry(), stack);
    }

    @Nullable
    public static UUID getInstanceID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.hasUUID("InstanceId") ? nbt.getUUID("InstanceId") : null;
    }

    public static int getSessionID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("SessionId") ? nbt.getInt("SessionId") : -1;
    }

    @Nullable
    public static UpgradeData<IPocketUpgrade> getUpgradeWithData(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound != null && compound.contains("Upgrade")) {
            IPocketUpgrade upgrade = (IPocketUpgrade)PocketUpgrades.instance().get(compound.getString("Upgrade"));
            return upgrade == null ? null : UpgradeData.of(upgrade, NBTUtil.getCompoundOrEmpty(compound, "UpgradeInfo"));
        } else {
            return null;
        }
    }
}
