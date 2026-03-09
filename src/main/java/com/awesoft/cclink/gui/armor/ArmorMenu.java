package com.awesoft.cclink.gui.armor;

import com.awesoft.cclink.registration.CCMenus;
import com.awesoft.cclink.item.ModularLinkArmor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import static com.awesoft.cclink.libs.MiscLib.itemToArmorType;
import static com.awesoft.cclink.libs.MiscLib.toEquipSlot;

public class ArmorMenu extends AbstractContainerMenu {
    public static final int SIDEBAR_WIDTH = 17;
    public static final int BORDER = 8;
    public static final int PLAYER_START_Y = 134-40;
    public static final int PLAYER_START_X = SIDEBAR_WIDTH + BORDER;
    public static final int UPGRADE_START_X = 211;

    private final ItemStackHandler handler;
    private final ItemStack armor;

    public ArmorMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(windowId, playerInv, buf.readItem());
    }

    public ArmorMenu(
            int id, Inventory playerInventory, ItemStack armor
    ) {
        super(CCMenus.ARMOR_MENU.get(), id);

        this.handler = ModularLinkArmor.getInventory(armor);
        this.armor = armor;

        // Player inventory
        for (var y = 0; y < 3; y++) {
            for (var x = 0; x < 9; x++) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, PLAYER_START_X + x * 18, PLAYER_START_Y + 1 + y * 18));
            }
        }

        // Player hotbar
        for (var x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, PLAYER_START_X + x * 18, PLAYER_START_Y + 3 * 18 + 5));
        }


        //armor modules

        int startX = 44+17;
        int startY = 11+16;
        int index = 0;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 5; x++) {
                this.addSlot(new ArmorUpgradeSlot(handler, index++, startX + x * 18, startY + y * 18, itemToArmorType(armor)));
            }
        }

    }

    private ItemStack tryItemMerge(Player player, int slotNum, int firstSlot, int lastSlot, boolean reverse) {
        var slot = slots.get(slotNum);
        var originalStack = ItemStack.EMPTY;
        if (slot != null && slot.hasItem()) {
            var clickedStack = slot.getItem();
            originalStack = clickedStack.copy();
            if (!moveItemStackTo(clickedStack, firstSlot, lastSlot, reverse)) {
                return ItemStack.EMPTY;
            }

            if (clickedStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (clickedStack.getCount() != originalStack.getCount()) {
                slot.onTake(player, clickedStack);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return originalStack;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int slotNum) {
        int playerInvStart = 0;
        int playerInvEnd = 27;
        int hotbarStart = playerInvEnd;
        int hotbarEnd = hotbarStart + 9;
        int droneStart = hotbarEnd;
        int droneEnd = droneStart + 5;

        if (slotNum >= droneStart && slotNum < droneEnd) {
            return tryItemMerge(player, slotNum, playerInvStart, hotbarEnd, true);
        } else if (slotNum >= playerInvStart && slotNum < hotbarEnd) {
            return tryItemMerge(player, slotNum, droneStart, droneEnd, false);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }


    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide && player.getItemBySlot(toEquipSlot(itemToArmorType(armor))) == armor) {
            ModularLinkArmor.saveInventory(armor, handler);
        }
    }

}
