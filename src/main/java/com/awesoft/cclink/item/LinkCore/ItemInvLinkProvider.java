package com.awesoft.cclink.item.LinkCore;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class ItemInvLinkProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final ItemStack stack;
    private final ItemStackHandler handler;
    private final LazyOptional<IItemHandler> optional;

    public static final String NBT_KEY = "Inventory";

    public ItemInvLinkProvider(ItemStack stack, int size) {
        this.stack = stack;

        this.handler = new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                saveToStack();
            }
        };

        if (stack.hasTag() && stack.getTag().contains(NBT_KEY)) {
            this.handler.deserializeNBT(stack.getTag().getCompound(NBT_KEY));
        }

        this.optional = LazyOptional.of(() -> handler);
    }


    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        handler.deserializeNBT(nbt);
    }

    private void saveToStack() {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(NBT_KEY, handler.serializeNBT());
    }

    public void writeToStack() {
        saveToStack();
    }
}
