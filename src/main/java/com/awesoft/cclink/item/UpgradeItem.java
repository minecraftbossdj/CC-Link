package com.awesoft.cclink.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeItem extends Item {
    int type;
    public UpgradeItem(Properties pProperties, int type) {
        super(pProperties);
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        tooltip.add(Component.literal("Fits into:"));

        if (stack.is(ItemTags.create(new ResourceLocation("cclink", "link_upgrades")))) {
            tooltip.add(Component.literal("  Link Core").withStyle(ChatFormatting.GRAY));
        }
        if (stack.is(ItemTags.create(new ResourceLocation("cclink", "armor_helmet_upgrades")))) {
            tooltip.add(Component.literal("  Link Visor").withStyle(ChatFormatting.GRAY));
        }
        if (stack.is(ItemTags.create(new ResourceLocation("cclink", "armor_chestplate_upgrades")))) {
            tooltip.add(Component.literal("  Link Harness").withStyle(ChatFormatting.GRAY));
        }
        if (stack.is(ItemTags.create(new ResourceLocation("cclink", "armor_leggings_upgrades")))) {
            tooltip.add(Component.literal("  Link Leggings").withStyle(ChatFormatting.GRAY));
        }
        if (stack.is(ItemTags.create(new ResourceLocation("cclink", "armor_boots_upgrades")))) {
            tooltip.add(Component.literal("  Link Boots").withStyle(ChatFormatting.GRAY));
        }
        if (stack.is(ItemTags.create(new ResourceLocation("cclink", "armor_upgrades")))) {
            tooltip.add(Component.literal("  Link Armor").withStyle(ChatFormatting.GRAY));
        }

        if (type == 1) {
            tooltip.add(Component.translatable("gui.cclink.lazy_mf").withStyle(ChatFormatting.DARK_GRAY)); //yes I really named it lazy_mf cry, I'm just stating the obvious smh
        } else if (type == 0) {
            tooltip.add(Component.translatable("gui.cclink.wip_texture").withStyle(ChatFormatting.DARK_GRAY));
        } else if (type == 2) {
            tooltip.add(Component.translatable("gui.cclink.creative_shield").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
