package com.awesoft.cclink.gui.armor;

import com.awesoft.cclink.gui.LinkMenu;
import dan200.computercraft.client.gui.AbstractComputerScreen;
import dan200.computercraft.client.gui.GuiSprites;
import dan200.computercraft.client.gui.widgets.ComputerSidebar;
import dan200.computercraft.client.gui.widgets.TerminalWidget;
import dan200.computercraft.client.render.RenderTypes;
import dan200.computercraft.client.render.SpriteRenderer;
import dan200.computercraft.shared.computer.inventory.AbstractComputerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.awesoft.cclink.gui.LinkMenu.BORDER;


public class ArmorScreen extends AbstractContainerScreen<ArmorMenu> {
    private static final ResourceLocation BACKGROUND_ADVANCED = new ResourceLocation("cclink", "textures/gui/armor_menu.png");

    private static final int TEX_WIDTH = 176;
    private static final int TEX_HEIGHT = 158;

    private static final int FULL_TEX_SIZE = 512;

    public ArmorScreen(ArmorMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageWidth = TEX_WIDTH + AbstractComputerMenu.SIDEBAR_WIDTH;
        imageHeight = TEX_HEIGHT;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        var texture = BACKGROUND_ADVANCED;
        graphics.blit(texture, leftPos + AbstractComputerMenu.SIDEBAR_WIDTH, topPos+59-40, 0, 0, 0, TEX_WIDTH, TEX_HEIGHT, FULL_TEX_SIZE, FULL_TEX_SIZE);

        var spriteRenderer = SpriteRenderer.createForGui(graphics, RenderTypes.GUI_SPRITES);
        graphics.flush();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int newX = 24;
        int newY = 84;
        graphics.drawString(font, playerInventoryTitle, newX, newY, 0x404040, false);
    }

}

