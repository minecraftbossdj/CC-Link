package com.awesoft.cclink.gui.armor;

import com.mojang.datafixers.util.Pair;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.impl.TurtleUpgrades;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ArmorUpgradeSlot extends SlotItemHandler {
    ArmorItem.Type type;
    public ArmorUpgradeSlot(ItemStackHandler itemStackHandler, int slot, int xPos, int yPos, ArmorItem.Type pType) {
        super(itemStackHandler, slot, xPos, yPos);
        this.type = pType;
    }

    TagKey<Item> HELM = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("cclink", "armor_helmet_upgrades"));
    TagKey<Item> CHEST = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("cclink", "armor_chestplate_upgrades"));
    TagKey<Item> LEGS = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("cclink", "armor_leggings_upgrades"));
    TagKey<Item> BOOTS = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("cclink", "armor_boots_upgrades"));

    TagKey<Item> GEN = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("cclink", "armor_upgrades"));

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.is(GEN)) {
            return true;
        } else {
            if (type == ArmorItem.Type.HELMET) {
                return stack.is(HELM);
            } else if (type == ArmorItem.Type.CHESTPLATE) {
                return stack.is(CHEST);
            } else if (type == ArmorItem.Type.LEGGINGS) {
                return stack.is(LEGS);
            } else if (type == ArmorItem.Type.BOOTS) {
                return stack.is(BOOTS);
            }
        }
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
