package com.awesoft.cclink.item;

import com.awesoft.cclink.registration.ItemRegistry;
import com.awesoft.cclink.gui.armor.ArmorMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ArmorConfiguratorItem extends Item {
    public ArmorConfiguratorItem(Properties pProperties) {
        super(pProperties);
    }

    public static void setMode(ItemStack stack, String mode) {
        stack.getOrCreateTag().putString("Mode", mode);
    }

    public static String getMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Mode")) {
            return tag.getString("Mode");
        }
        return "helmet";
    }

    private EquipmentSlot fromString(String armor) {
        if (armor.equalsIgnoreCase("helmet")) return EquipmentSlot.HEAD;
        if (armor.equalsIgnoreCase("chestplate")) return EquipmentSlot.CHEST;
        if (armor.equalsIgnoreCase("leggings")) return EquipmentSlot.LEGS;
        if (armor.equalsIgnoreCase("boots")) return EquipmentSlot.FEET;

        return null;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);



        if (pPlayer.isCrouching()) {
            if (getMode(itemstack).equals("helmet")) {
                setMode(itemstack,"chestplate");
                pPlayer.displayClientMessage(Component.translatable("actionbar.setmode.chestplate"), true);
            } else if (getMode(itemstack).equals("chestplate")) {
                setMode(itemstack,"leggings");
                pPlayer.displayClientMessage(Component.translatable("actionbar.setmode.leggings"), true);
            } else if (getMode(itemstack).equals("leggings")) {
                setMode(itemstack,"boots");
                pPlayer.displayClientMessage(Component.translatable("actionbar.setmode.boots"), true);
            } else if (getMode(itemstack).equals("boots")) {
                setMode(itemstack,"helmet");
                pPlayer.displayClientMessage(Component.translatable("actionbar.setmode.helmet"), true);
            }
        } else {
            EquipmentSlot equipSlot = fromString(getMode(itemstack));
            ItemStack armorItem = pPlayer.getItemBySlot(equipSlot);
            if (armorItem.is(ItemRegistry.MODULAR_LINK_HELMET.get()) || armorItem.is(ItemRegistry.MODULAR_LINK_CHESTPLATE.get()) || armorItem.is(ItemRegistry.MODULAR_LINK_LEGGINGS.get()) || armorItem.is(ItemRegistry.MODULAR_LINK_BOOTS.get())) {
                if (pLevel.isClientSide) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.literal("");
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player p) {
                            return new ArmorMenu(windowId, inv, p.getItemBySlot(equipSlot));
                        }
                    }, buf -> {
                        buf.writeItem(pPlayer.getItemBySlot(equipSlot));
                    });
                }
            }

        }


        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

}
