package com.awesoft.cclink.hudoverlay;

import com.awesoft.cclink.hudoverlay.packets.HUDItemPacket;
import com.awesoft.cclink.hudoverlay.packets.HUDRectanglePacket;
import com.awesoft.cclink.hudoverlay.packets.HUDTextPacket;
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


        // Render rectangles
        Map<String, RectangleElement> rectangleElements = playerRectangleElements.get(playerUUID);
        if (rectangleElements != null) {
            for (RectangleElement element : rectangleElements.values()) {
                poseStack.pushPose();

                // Draw the rectangle
                guiGraphics.fill(element.getX1(), element.getY1(), element.getX2(), element.getY2(), element.getColor());

                poseStack.popPose();
            }
        }



        // Render text
        Map<String, TextElement> textElements = playerTextElements.get(playerUUID);
        if (textElements != null) {
            for (TextElement element : textElements.values()) {
                poseStack.pushPose();

                // Apply scaling
                poseStack.scale(element.getScale(), element.getScale(), 1.0F);

                // Adjust position for scaling
                float scaledX = element.getX() / element.getScale();
                float scaledY = element.getY() / element.getScale();


                // Draw the text
                guiGraphics.drawString(mc.font, element.getText(), (int) scaledX, (int) scaledY, element.getColor(), false);

                poseStack.popPose();
            }
        }

    // Render rightbound text
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
        ResourceLocation itemId = new ResourceLocation(itemName); // e.g., "minecraft:dirt"
        Item item = BuiltInRegistries.ITEM.get(itemId);

        if (item != null) {
            return new ItemStack(item); // Create an ItemStack from the Item
        } else {
            System.out.println("Invalid item name: " + itemName);
            return null;
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



    // Method to add or update a rectangle element for a specific player
    public static void addOrUpdateRectangleElement(UUID playerUUID, String key, int x1, int y1, int x2, int y2, int rgbColor, int transparency) {
        int color = ColorUtil.toArgb(rgbColor, transparency);
        playerRectangleElements.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(key, new RectangleElement(x1, y1, x2, y2, color));


    }

    // Method to remove a rectangle element for a specific player by key
    public static void removeRectangleElement(UUID playerUUID, String key) {
        Map<String, RectangleElement> rectangleElements = playerRectangleElements.get(playerUUID);
        if (rectangleElements != null) {
            rectangleElements.remove(key);
            // Clean up the player's map if it's empty
            if (rectangleElements.isEmpty()) {
                playerRectangleElements.remove(playerUUID);
            }
        }
    }

    // Method to clear all rectangle elements for a specific player
    public static void clearRectangleElementsForPlayer(UUID playerUUID) {
        playerRectangleElements.remove(playerUUID);
    }

    // Method to add or update a text element for a specific player
    public static void addOrUpdateTextElement(UUID playerUUID, String key, String text, int x, int y, int color, float scale) {
        playerTextElements.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(key, new TextElement(text, x, y, color, scale));
    }

    // Method to remove a text element for a specific player by key
    public static void removeTextElement(UUID playerUUID, String key) {
        Map<String, TextElement> textElements = playerTextElements.get(playerUUID);
        if (textElements != null) {
            textElements.remove(key);
            // Clean up the player's map if it's empty
            if (textElements.isEmpty()) {
                playerTextElements.remove(playerUUID);
            }
        }
    }

    // Method to clear all text elements for a specific player
    public static void clearTextElementsForPlayer(UUID playerUUID) {
        playerTextElements.remove(playerUUID);
    }


    // Method to handle a HUDTextPacket
    public static void handleTextPacket(HUDTextPacket packet) {
        addOrUpdateTextElement(packet.getPlayerUUID(), packet.getElementID(), packet.getText(), packet.getX(), packet.getY(), packet.getColor(), packet.getScale());
    }

    // Method to handle a HUDItemPacket
    public static void handleItemPacket(HUDItemPacket packet) {
        addOrUpdateItemElement(packet.getPlayerUUID(), packet.getElementID(), packet.getItemResource(), packet.getX(), packet.getY());
    }

    // Method to handle a HUDRectanglePacket
    public static void handleRectanglePacket(HUDRectanglePacket packet) {
        addOrUpdateRectangleElement(packet.getPlayerUUID(), packet.getElementID(), packet.getX1(), packet.getY1(), packet.getX2(), packet.getY2(), packet.getColor(), packet.getTransparency());
    }
//remove
    public static void handleRemoveTextPacket(HUDTextPacket packet) {
        removeTextElement(packet.getPlayerUUID(), packet.getElementID());
    }

    // Method to handle a HUDItemPacket
    public static void handleRemoveItemPacket(HUDItemPacket packet) {
        removeItemElement(packet.getPlayerUUID(), packet.getElementID());
    }

    // Method to handle a HUDRectanglePacket
    public static void handleRemoveRectanglePacket(HUDRectanglePacket packet) {
        removeRectangleElement(packet.getPlayerUUID(), packet.getElementID());
    }
    //remove all

    public static void handleRemoveAllTextPacket(HUDTextPacket packet) {
        clearTextElementsForPlayer(packet.getPlayerUUID());
    }

    // Method to handle a HUDItemPacket
    public static void handleRemoveAllItemPacket(HUDItemPacket packet) {
        clearItemElementsForPlayer(packet.getPlayerUUID());
    }

    // Method to handle a HUDRectanglePacket
    public static void handleRemoveAllRectanglePacket(HUDRectanglePacket packet) {
        clearRectangleElementsForPlayer(packet.getPlayerUUID());
    }

}