package com.awesoft.cclink.hudoverlay;


import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.libs.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = "cclink", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HUDOverlay {
    private static final Map<UUID, Map<String, RectangleElement>> playerRectangleElements = new HashMap<>();
    private static final Map<UUID, Map<String, TextElement>> playerTextElements = new HashMap<>();

    private static final Map<UUID, Map<String, RightboundTextElement>> playerRightboundTextElements = new HashMap<>();
    private static final Map<UUID, Map<String, ItemElement>> playerItemElements = new HashMap<>();
    private static final Map<UUID, PlayerGuiDimensions> playerGuiDimensions = new HashMap<>();

    private static ServerPlayer getServerPlayer(UUID playerUUID) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            renderHUD(event.getGuiGraphics(), player.getUUID());
        }
    }

    private static void renderHUD(GuiGraphics guiGraphics, UUID playerUUID) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();


        Map<String, RectangleElement> rectangleElements = playerRectangleElements.get(playerUUID);
        if (rectangleElements != null) {
            for (RectangleElement element : rectangleElements.values()) {
                poseStack.pushPose();

                guiGraphics.fill(element.getX1(), element.getY1(), element.getX2(), element.getY2(), element.getColor());

                poseStack.popPose();
            }
        }



        // text
        Map<String, TextElement> textElements = playerTextElements.get(playerUUID);
        if (textElements != null) {
            for (TextElement element : textElements.values()) {
                poseStack.pushPose();

                poseStack.scale(element.getScale(), element.getScale(), 1.0F);

                float scaledX = element.getX() / element.getScale();
                float scaledY = element.getY() / element.getScale();


                guiGraphics.drawString(mc.font, element.getText(), (int) scaledX, (int) scaledY, element.getColor(), false);

                poseStack.popPose();
            }
        }

    //rightbound text
        Map<String, RightboundTextElement> rightboundTextElements = playerRightboundTextElements.get(playerUUID);
        if (rightboundTextElements != null) {
            for (RightboundTextElement element : rightboundTextElements.values()) {
                poseStack.pushPose();
                poseStack.scale(element.getScale(), element.getScale(), 1.0F);
                float scaledX = (element.getX() - mc.font.width(element.getText())) / element.getScale();
                float scaledY = element.getY() / element.getScale();
                guiGraphics.drawString(mc.font, element.getText(), (int) scaledX, (int) scaledY, element.getColor(), false);
                poseStack.popPose();
            }
        }


        //Render Items
        Map<String, ItemElement> itemElements = playerItemElements.get(playerUUID);
        if (itemElements != null) {
            for (ItemElement element : itemElements.values()) {
                guiGraphics.renderFakeItem(element.getItemStack(), element.getX(), element.getY());
            }
        }

        
            int guiWidth = guiGraphics.guiWidth();
            int guiHeight = guiGraphics.guiHeight();
            playerGuiDimensions.put(playerUUID, new PlayerGuiDimensions(guiWidth, guiHeight));

    }


    public static void addOrUpdateRightboundTextElement(UUID playerUUID, String key, String text, int x, int y, int color, float scale) {
        playerRightboundTextElements.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(key, new RightboundTextElement(text, x, y, color, scale));
    }

    public static void removeRightboundTextElement(UUID playerUUID, String key) {
        Map<String, RightboundTextElement> rightboundTextElements = playerRightboundTextElements.get(playerUUID);
        if (rightboundTextElements != null) {
            rightboundTextElements.remove(key);
            if (rightboundTextElements.isEmpty()) {
                playerRightboundTextElements.remove(playerUUID);
            }
        }
    }

    public static void clearRightboundTextElementsForPlayer(UUID playerUUID) {
        playerRightboundTextElements.remove(playerUUID);
    }

    public static void addOrUpdateItemElement(UUID playerUUID, String key, String itemName, int x, int y) {
        ItemStack itemStack = createItemStackFromString(itemName);
        if (itemStack != null) {
            playerItemElements.computeIfAbsent(playerUUID, k -> new HashMap<>())
                    .put(key, new ItemElement(itemStack, x, y));
        }
    }

    private static ItemStack createItemStackFromString(String itemName) {
        ResourceLocation itemId = new ResourceLocation(itemName);
        Item item = BuiltInRegistries.ITEM.get(itemId);

        if (item != null) {
            return new ItemStack(item);
        } else {
            return new ItemStack(Blocks.AIR);
        }
    }

    public static void removeItemElement(UUID playerUUID, String key) {
        Map<String, ItemElement> itemElements = playerItemElements.get(playerUUID);
        if (itemElements != null) {
            itemElements.remove(key);
        }
    }

    public static void clearItemElementsForPlayer(UUID playerUUID) {
        playerItemElements.remove(playerUUID);
    }



    public static void addOrUpdateRectangleElement(UUID playerUUID, String key, int x1, int y1, int x2, int y2, int rgbColor, int transparency) {
        int color = ColorUtil.toArgb(rgbColor, transparency);
        playerRectangleElements.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(key, new RectangleElement(x1, y1, x2, y2, color));
    }

    public static void removeRectangleElement(UUID playerUUID, String key) {
        Map<String, RectangleElement> rectangleElements = playerRectangleElements.get(playerUUID);
        if (rectangleElements != null) {
            rectangleElements.remove(key);
            if (rectangleElements.isEmpty()) {
                playerRectangleElements.remove(playerUUID);
            }
        }
    }

    public static void clearRectangleElementsForPlayer(UUID playerUUID) {
        playerRectangleElements.remove(playerUUID);
    }

    public static void addOrUpdateTextElement(UUID playerUUID, String key, String text, int x, int y, int color, float scale) {
        playerTextElements.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(key, new TextElement(text, x, y, color, scale));
    }

    public static void removeTextElement(UUID playerUUID, String key) {
        Map<String, TextElement> textElements = playerTextElements.get(playerUUID);
        if (textElements != null) {
            textElements.remove(key);
            if (textElements.isEmpty()) {
                playerTextElements.remove(playerUUID);
            }
        }
    }

    // Method to clear all text elements for a specific player
    public static void clearTextElementsForPlayer(UUID playerUUID) {
        playerTextElements.remove(playerUUID);
    }

}