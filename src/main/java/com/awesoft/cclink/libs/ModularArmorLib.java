package com.awesoft.cclink.libs;

import com.awesoft.cclink.item.ModularLinkArmor;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.awesoft.cclink.libs.MiscLib.toEquipSlot;

public class ModularArmorLib {
    public static NonNullList<ItemStack> getCombinedArmorModules(Player player) {
        NonNullList<ItemStack> combined = NonNullList.create();

        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof ModularLinkArmor) {
                ItemStackHandler handler = ModularLinkArmor.getInventory(armorStack);
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack slotStack = handler.getStackInSlot(i);
                    if (!slotStack.isEmpty()) {
                        combined.add(slotStack);
                    }
                }
            }
        }

        return combined;
    }

    public static boolean hasModule(ServerPlayer plr, Item item) {
        AtomicBoolean hasModuleBool = new AtomicBoolean(false);
        getCombinedArmorModules(plr).forEach(stack -> {
            if (stack.getItem().equals(item)) hasModuleBool.set(true);
        });
        return hasModuleBool.get();
    }

    public static boolean isModular(ServerPlayer plr, EquipmentSlot slot) {
        return plr.getItemBySlot(slot).getItem() instanceof ModularLinkArmor;
    }

    public static boolean isFullModular(ServerPlayer plr) {
        return isModular(plr, EquipmentSlot.HEAD) && isModular(plr, EquipmentSlot.CHEST) && isModular(plr, EquipmentSlot.LEGS) && isModular(plr, EquipmentSlot.FEET);
    }

    public static IEnergyStorage getEnergyFromArmor(ServerPlayer plr, EquipmentSlot slot) {
        AtomicReference<IEnergyStorage> energyStorage = new AtomicReference<>();
        ItemStack armorStack = plr.getItemBySlot(slot);
        if (armorStack.getItem() instanceof ModularLinkArmor) {
            armorStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage::set);
        }

        return energyStorage.get();
    }
}
