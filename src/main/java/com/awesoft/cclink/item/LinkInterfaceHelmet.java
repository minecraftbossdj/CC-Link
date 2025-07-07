package com.awesoft.cclink.item;

import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class LinkInterfaceHelmet extends ArmorItem implements ICurioItem {
    public LinkInterfaceHelmet(Item.Properties properties, ArmorMaterial material) {
        super(material, ArmorItem.Type.HELMET, properties);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}

