package com.awesoft.cclink.hudoverlay;

import net.minecraft.world.item.ItemStack;

class ItemElement {
    private final ItemStack itemStack;
    private final int x, y;

    public ItemElement(ItemStack itemStack, int x, int y) {
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}