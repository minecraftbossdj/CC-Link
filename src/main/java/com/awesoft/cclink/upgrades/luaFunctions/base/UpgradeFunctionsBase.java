package com.awesoft.cclink.upgrades.luaFunctions.base;

import com.awesoft.cclink.libs.ComputerLib;
import com.awesoft.cclink.registration.ItemRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Set;

public class UpgradeFunctionsBase {

    public Entity entity;
    public boolean isIntegrated; //todo: fix for armor
    public Item UPGRADE = ItemRegistry.CHATTY_UPGRADE.get();

    public final Container getLinkInventory() {
        if (isIntegrated) {
            return ComputerLib.getContainerFromIntegratedLinkHolder(entity);
        }
        return ComputerLib.getContainerFromLinkHolder(entity);
    }

    public final boolean checkUpgrade() {
        if (!(entity instanceof Player)) return false;
        Set<Item> items = Set.of(UPGRADE);

        return getLinkInventory().hasAnyOf(items);
    }
}
