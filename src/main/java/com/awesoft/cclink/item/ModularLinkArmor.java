package com.awesoft.cclink.item;

import com.awesoft.cclink.libs.MiscLib;
import com.awesoft.cclink.modularArmor.ModularArmorEnergy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModularLinkArmor extends ArmorItem {

    private static final int MODULE_SLOTS = 15;

    public ModularLinkArmor(Properties pProperties, ArmorItem.Type aType, ArmorMaterial pMaterial) {
        super(pMaterial, aType, pProperties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {

            private final ItemStackHandler handler = new ItemStackHandler(MODULE_SLOTS) {
                @Override
                protected void onContentsChanged(int slot) {
                    stack.getOrCreateTag().put("Modules", this.serializeNBT());
                }
            };

            {
                if (stack.hasTag() && stack.getTag().contains("Modules")) {
                    handler.deserializeNBT(stack.getTag().getCompound("Modules"));
                }
            }

            private final ModularArmorEnergy energy = new ModularArmorEnergy(stack,handler);

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    return LazyOptional.of(() -> handler).cast();
                } else if (cap == ForgeCapabilities.ENERGY) {
                    return LazyOptional.of(() -> new ModularArmorEnergy(stack,handler) {
                        @Override
                        public int getMaxEnergyStored() {
                            return super.getMaxEnergy();
                        }
                    }).cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    public static void saveInventory(ItemStack stack, ItemStackHandler handler) {
        stack.getOrCreateTag().put("Modules", handler.serializeNBT());
    }

    public static ItemStackHandler getInventory(ItemStack stack) {
        ItemStackHandler handler = new ItemStackHandler(MODULE_SLOTS);
        if (stack.hasTag() && stack.getTag().contains("Modules")) {
            handler.deserializeNBT(stack.getTag().getCompound("Modules"));
        }
        return handler;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
                .map(energy -> energy.getMaxEnergyStored() > 100)
                .orElse(false);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
                .map(energy -> (int)(13.0 * energy.getEnergyStored() / energy.getMaxEnergyStored()))
                .orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF0000;
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
            tooltip.add(
                    Component.literal("Energy: ")
                        .append(Component.literal(MiscLib.formatNumber(energy.getEnergyStored())+"FE").withStyle(ChatFormatting.DARK_RED))
                        .append(Component.literal(" / ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(MiscLib.formatNumber(energy.getMaxEnergyStored())+"FE").withStyle(ChatFormatting.DARK_RED))
            );
        });
    }
}
