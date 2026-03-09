package com.awesoft.cclink.peripherals.linkArmorManager;

import com.awesoft.cclink.item.ModularLinkArmor;
import com.awesoft.cclink.libs.ModularArmorLib;
import com.awesoft.cclink.upgrades.luaFunctions.*;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.awesoft.cclink.libs.MiscLib.toEquipSlot;

public class LinkArmorManagerPeripheral implements IPeripheral {

    IPocketAccess access;
    private int compId;
    Object periphClass;

    public LinkArmorManagerPeripheral(IPocketAccess access)
    {
        this.access=access;
    }

    @Override
    public String getType() {
        return "link_armor";
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof LinkArmorManagerPeripheral;
    }

    @Override
    public void attach(IComputerAccess computer) {
        IPeripheral.super.attach(computer);

        compId = computer.getID();
    }

    public final boolean isEntityAlive() {
        if (access.getEntity() == null) {
            return false;
        } else {
            return access.getEntity().isAlive();
        }
    }

    public final ServerPlayer getServPlayer() {
        if (isEntityAlive()) {
            if (access.getEntity() instanceof ServerPlayer player) {
                return player;
            }
        }
        return null;
    }

    public final boolean hasUpgradeNonLua(String upgradeid) {
        if (!isEntityAlive()) return false;
        Item id = BuiltInRegistries.ITEM.get(new ResourceLocation(upgradeid));
        Set<Item> items = Set.of(id);

        boolean hasItem = false;

        NonNullList<ItemStack> itemStacks = ModularArmorLib.getCombinedArmorModules(getServPlayer());

        for (int i=0; i < itemStacks.size(); i++) {
            if (itemStacks.get(i).is(id)) {
                hasItem = true;
            }
        }

        return hasItem;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult listUpgrades() {
        if (!isEntityAlive()) return null;

        NonNullList<ItemStack> upgrades = ModularArmorLib.getCombinedArmorModules(getServPlayer());

        ArrayList<String> result = new ArrayList<>();
        var size = upgrades.size();
        for (ItemStack stack : upgrades) {
            if (!stack.isEmpty()) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                result.add(id.toString());
            }
        }
        return MethodResult.of(true, result);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult hasUpgrade(String upgradeid) {
        if (!isEntityAlive()) return null;
        return MethodResult.of(hasUpgradeNonLua(upgradeid));
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getArmorFunctions() {
        if (!isEntityAlive()) return null;
        Map<String, Object> functions = new HashMap<>();

        if (hasUpgradeNonLua("cclink:scanner_upgrade")) {
            ScannerUpgradeFunctions funcs = new ScannerUpgradeFunctions(getServPlayer(),compId, false);
            functions.put("scanner",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:sensor_upgrade")) {
            SensorUpgradeFunctions funcs = new SensorUpgradeFunctions(getServPlayer(),compId, false);
            functions.put("sensor",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:introspection_upgrade")) {
            IntrospectionUpgradeFunctions funcs = new IntrospectionUpgradeFunctions(getServPlayer(), false);
            functions.put("introspection",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:world_upgrade")) {
            WorldUpgradeFunctions funcs = new WorldUpgradeFunctions(getServPlayer(), false);
            functions.put("world",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:overlay_upgrade")) {
            OverlayUpgradeFunctions funcs = new OverlayUpgradeFunctions(getServPlayer(), false);
            functions.put("overlay",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:kinetic_upgrade")) {
            KineticUpgradeFunctions funcs = new KineticUpgradeFunctions(getServPlayer(),false, false);
            functions.put("kinetic",funcs.getFunctions());
        }
        //TODO: lasergun

        return functions;
    }

    @LuaFunction(mainThread = true)
    public final int getEnergy(String type) {
        if (!isEntityAlive()) return 0;
        if (toEquipSlot(type) == null) return 0;


        AtomicInteger returnInt = new AtomicInteger();
        returnInt.set(0);
        ItemStack armorStack = getServPlayer().getItemBySlot(toEquipSlot(type));
        if (armorStack.getItem() instanceof ModularLinkArmor) {
            armorStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                returnInt.set(energy.getEnergyStored());
            });
        }
        return returnInt.get();
    }

    @LuaFunction(mainThread = true)
    public final int getMaxEnergy(String type) {
        if (!isEntityAlive()) return 0;
        if (toEquipSlot(type) == null) return 0;

        AtomicInteger returnInt = new AtomicInteger();
        returnInt.set(0);
        ItemStack armorStack = getServPlayer().getItemBySlot(toEquipSlot(type));
        if (armorStack.getItem() instanceof ModularLinkArmor) {
            armorStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                returnInt.set(energy.getMaxEnergyStored());
            });
        }
        return returnInt.get();

    }


    //todo: add shield shutoff/status
}
