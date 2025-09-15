package com.awesoft.cclink.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeItem extends Item {
    boolean isLaser;
    public UpgradeItem(Properties pProperties, boolean isLaser) {
        super(pProperties);
        this.isLaser = isLaser;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (isLaser) {
            pTooltipComponents.add(Component.translatable("gui.cclink.lazy_mf").withStyle(ChatFormatting.DARK_GRAY)); //yes i really named it lazy_mf cry, I'm just stating the obvious smh
        } else {
            pTooltipComponents.add(Component.translatable("gui.cclink.wip_texture").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
