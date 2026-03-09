package com.awesoft.cclink.modularArmor;

import com.awesoft.cclink.CCLinkConfig;
import com.awesoft.cclink.registration.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class ModularArmorEnergy implements IEnergyStorage {

    private final ItemStack stack;
    private final ItemStackHandler handler;
    private int energy;
    private int baseMaxEnergy = 100;

    private final String NBT_KEY = "Energy";

    public ModularArmorEnergy(ItemStack stack, ItemStackHandler handler) {
        this.stack = stack;
        this.handler = handler;
        if (stack.hasTag() && stack.getTag().contains(NBT_KEY)) {
            this.energy = stack.getTag().getInt(NBT_KEY);
        } else {
            this.energy = 0;
        }
    }

    public int getMaxEnergy() {
        int bonus = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slot = handler.getStackInSlot(i);
            if (slot.is(ItemRegistry.BATTERY_UPGRADE_1.get())) bonus += CCLinkConfig.TIER1_MAX_ENERGY.get();
            if (slot.is(ItemRegistry.BATTERY_UPGRADE_2.get())) bonus += CCLinkConfig.TIER2_MAX_ENERGY.get();
            if (slot.is(ItemRegistry.BATTERY_UPGRADE_3.get())) bonus += CCLinkConfig.TIER3_MAX_ENERGY.get();
        }
        return baseMaxEnergy + bonus;
    }

    private void save() {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(NBT_KEY, energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int maxEnergy = getMaxEnergy();
        int energyReceived = Math.min(maxEnergy - energy, maxReceive);
        if (!simulate) {
            energy += energyReceived;
            save();
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate) {
            energy -= energyExtracted;
            save();
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() { return energy; }

    @Override
    public int getMaxEnergyStored() {
        int bonus = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slot = handler.getStackInSlot(i);
            if (slot.is(ItemRegistry.BATTERY_UPGRADE_1.get())) bonus += CCLinkConfig.TIER1_MAX_ENERGY.get();
            if (slot.is(ItemRegistry.BATTERY_UPGRADE_2.get())) bonus += CCLinkConfig.TIER2_MAX_ENERGY.get();
            if (slot.is(ItemRegistry.BATTERY_UPGRADE_3.get())) bonus += CCLinkConfig.TIER3_MAX_ENERGY.get();
        }
        return baseMaxEnergy + bonus;
    }

    @Override
    public boolean canExtract() { return true; }

    @Override
    public boolean canReceive() { return true; }
}

