package com.awesoft.cclink.libs;

import com.awesoft.cclink.registration.ItemRegistry;
import com.awesoft.cclink.item.ModularLinkArmor;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MiscLib {
    
    public static Direction toDirection(String dir) {
        if (dir.equalsIgnoreCase("north")) return Direction.NORTH;
        if (dir.equalsIgnoreCase("south")) return Direction.SOUTH;
        if (dir.equalsIgnoreCase("east")) return Direction.EAST;
        if (dir.equalsIgnoreCase("west")) return Direction.WEST;
        if (dir.equalsIgnoreCase("up")) return Direction.UP;
        if (dir.equalsIgnoreCase("down")) return Direction.DOWN;
        
        return null;
    }

    public static EquipmentSlot toEquipSlot(ArmorItem.Type type) {
        if (type == ArmorItem.Type.HELMET) return EquipmentSlot.HEAD;
        if (type == ArmorItem.Type.CHESTPLATE) return EquipmentSlot.CHEST;
        if (type == ArmorItem.Type.LEGGINGS) return EquipmentSlot.LEGS;
        if (type == ArmorItem.Type.BOOTS) return EquipmentSlot.FEET;

        return null;
    }

    public static EquipmentSlot toEquipSlot(String type) {
        if (type.equalsIgnoreCase("helmet")) return EquipmentSlot.HEAD;
        if (type.equalsIgnoreCase("chestplate")) return EquipmentSlot.CHEST;
        if (type.equalsIgnoreCase("leggings")) return EquipmentSlot.LEGS;
        if (type.equalsIgnoreCase("boots")) return EquipmentSlot.FEET;

        return null;
    }

    public static ArmorItem.Type itemToArmorType(ItemStack armor) {
        if (armor.is(ItemRegistry.MODULAR_LINK_HELMET.get())) return ArmorItem.Type.HELMET;
        if (armor.is(ItemRegistry.MODULAR_LINK_CHESTPLATE.get())) return ArmorItem.Type.CHESTPLATE;
        if (armor.is(ItemRegistry.MODULAR_LINK_LEGGINGS.get())) return ArmorItem.Type.LEGGINGS;
        if (armor.is(ItemRegistry.MODULAR_LINK_BOOTS.get())) return ArmorItem.Type.BOOTS;

        return null;
    }

    public static boolean isArmorPiercing(DamageSource damage) {
        return damage.is(DamageTypeTags.BYPASSES_ARMOR) && !damage.is(DamageTypeTags.IS_FALL) && damage.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damage.is(DamageTypes.FLY_INTO_WALL) && !damage.is(DamageTypes.BAD_RESPAWN_POINT) && !damage.is(DamageTypes.MAGIC);
    }

    public static String formatNumber(long num) {
        if (num < 1000) return String.valueOf(num);
        int exp = (int) (Math.log(num) / Math.log(1000));
        char suffix = "kMBTPE".charAt(exp - 1); // k=thousand, M=million, B=billion, etc.
        double value = num / Math.pow(1000, exp);

        String formatted = String.format("%.1f", value).replaceAll("\\.0$", "");
        return formatted + suffix;
    }

}
