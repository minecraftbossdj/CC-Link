package com.awesoft.cclink.item;

import com.awesoft.cclink.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.*;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;

public class LinkInterfaceHelmet extends ArmorItem implements ICurioItem {
    public LinkInterfaceHelmet(Item.Properties properties, ArmorMaterial material) {
        super(material, ArmorItem.Type.HELMET, properties);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}

