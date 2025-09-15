package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.hudoverlay.packets.HUDOverlayUpdatePacket;
import com.awesoft.cclink.hudoverlay.packets.PacketManager;
import com.awesoft.cclink.libs.PacketCooldownManager;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class OverlayUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;

    public OverlayUpgradeFunctions(Entity entity) {
        this.entity = entity;
    }

    public final ServerPlayer getPlayer() {
        if (entity instanceof ServerPlayer player) {
            return player;
        } else {
            return null;
        }
    }

    public final Inventory getInventory() {
        if (getPlayer() == null) {return null;}
        return getPlayer().getInventory();
    }

    public PlayerEnderChestContainer getEnderInventory() {
        return getPlayer() != null ? getPlayer().getEnderChestInventory() : null;
    }

    public final UUID getOwnerUUID() {
        if (getPlayer() != null) {
            return getPlayer().getUUID();
        }
        return null;
    }

    List<HUDOverlayUpdatePacket.Entry> entries = new ArrayList<>();

    ILuaFunction send = args -> {
        if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
            if (getPlayer() != null) {
                HUDOverlayUpdatePacket packet = new HUDOverlayUpdatePacket(Objects.requireNonNull(getPlayer()).getUUID(), entries);

                PacketManager.sendToClient(getPlayer().getUUID(), packet);

                return MethodResult.of(true);
            }
            return MethodResult.of(false);
        } else {
            return MethodResult.of(false,"On cooldown!");
        }
    };

    ILuaFunction addOrUpdateTextElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                args.getString(0),
                args.getString(1),
                args.getInt(2),
                args.getInt(3),
                args.getInt(4),
                (float)args.getDouble(5),
                false,
                false,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction removeTextElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                args.getString(0),
                "",
                0,0,0,
                0.0f,
                false,
                true,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction clearText = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                "",
                "",
                0,0,0,
                0.0f,
                false,
                false,
                true
        ));
        return MethodResult.of(true);
    };



    //rightbound string :wilted_rose:
    ILuaFunction addOrUpdateRightboundTextElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                args.getString(0),
                args.getString(1),
                args.getInt(2),
                args.getInt(3),
                args.getInt(4),
                (float)args.getDouble(5),
                true,
                false,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction removeRightboundTextElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                args.getString(0),
                "",
                0,0,0,
                0.0f,
                true,
                true,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction clearRightboundText = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                "",
                "",
                0,0,0,
                0.0f,
                true,
                false,
                true
        ));
        return MethodResult.of(true);
    };



    //box elementer
    ILuaFunction addOrUpdateRectElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.rect(
                args.getString(0),
                args.getInt(1),
                args.getInt(2),
                args.getInt(3),
                args.getInt(4),
                args.getInt(5),
                args.getInt(6),
                false,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction removeRectElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.rect(
                args.getString(0),
                0,0,0,0,0,0,
                true,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction clearRect = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.rect(
                "",
                0,0,0,0,0,0,
                false,
                true
        ));
        return MethodResult.of(true);
    };



    ILuaFunction addOrUpdateItemElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.item(
                args.getString(0),
                args.getString(1),
                args.getInt(2),
                args.getInt(3),
                false,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction removeItemElement = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.item(
                args.getString(0),
                "",0,0,
                true,
                false
        ));
        return MethodResult.of(true);
    };


    ILuaFunction clearItem = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.item(
                "","",0,0,
                false,
                true
        ));
        return MethodResult.of(true);
    };


    ILuaFunction clearAll = args -> {
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                "",
                "",
                0,0,0,
                0.0f,
                false,
                false,
                true
        )); //text
        entries.add(HUDOverlayUpdatePacket.Entry.text(
                "",
                "",
                0,0,0,
                0.0f,
                true,
                false,
                true
        )); //rightbound text
        entries.add(HUDOverlayUpdatePacket.Entry.rect(
                "",
                0,0,0,0,0,0,
                false,
                true
        )); //rect
        entries.add(HUDOverlayUpdatePacket.Entry.item(
                "","",0,0,
                false,
                true
        )); //item
        return MethodResult.of(true);
    };

    public Map<String, Object> getFunctions() {
        functions.put("addOrUpdateTextElement",addOrUpdateTextElement);
        functions.put("removeTextElement",removeTextElement);
        functions.put("clearText",clearText);
        functions.put("addOrUpdateRightboundTextElement",addOrUpdateRightboundTextElement);
        functions.put("removeRightboundTextElement",removeRightboundTextElement);
        functions.put("clearRightboundText",clearRightboundText);
        functions.put("addOrUpdateRectElement",addOrUpdateRectElement);
        functions.put("removeRectElement",removeRectElement);
        functions.put("clearRect",clearRect);
        functions.put("addOrUpdateItemElement",addOrUpdateItemElement);
        functions.put("removeItemElement",removeItemElement);
        functions.put("clearItem",clearItem);
        functions.put("clearAll",clearAll);
        functions.put("send",send);
        return functions;
    }

}
