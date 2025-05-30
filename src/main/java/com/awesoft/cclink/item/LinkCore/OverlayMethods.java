package com.awesoft.cclink.item.LinkCore;

import com.awesoft.cclink.hudoverlay.packets.PacketManager;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;

import java.util.UUID;

public class OverlayMethods {

    public static MethodResult addOrUpdateTextElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        String newText = args.getString(1);
        int newX = args.getInt(2);
        int newY = args.getInt(3);
        int newColor = args.getInt(4);
        int scale = args.getInt(5);

        if (isPlayerAlive) {
            PacketManager.sendTextPacketToPlayer(getOwnerUUID,key, newText, newX, newY, newColor, scale);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    };

    public static MethodResult removeTextElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        if (isPlayerAlive) {
            PacketManager.removeTextElementPacket(getOwnerUUID,key);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    };

    public static MethodResult addOrUpdateRightboundTextElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        String newText = args.getString(1);
        int newX = args.getInt(2);
        int newY = args.getInt(3);
        int newColor = args.getInt(4);
        int scale = args.getInt(5);

        if (isPlayerAlive) {
            PacketManager.sendRightboundTextPacketToPlayer(getOwnerUUID,key, newText, newX, newY, newColor, scale);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    };

    public static MethodResult removeRightboundTextElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        if (isPlayerAlive) {
            PacketManager.removeRightboundTextElementPacket(getOwnerUUID,key);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    };

    public static MethodResult addOrUpdateRectElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        int x1 = args.getInt(1);
        int y1 = args.getInt(2);
        int x2 = args.getInt(3);
        int y2 = args.getInt(4);
        int newColor = args.getInt(5);
        int transparency = args.getInt(6);

        if (isPlayerAlive) {
            PacketManager.sendRectPacketToPlayer(getOwnerUUID,key,x1,y1,x2,y2,newColor,transparency);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

    public static MethodResult removeRectElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        if (isPlayerAlive) {
            PacketManager.removeRectangleElementPacket(getOwnerUUID,key);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

    public static MethodResult addOrUpdateItemElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        String id = args.getString(1);
        int x = args.getInt(2);
        int y = args.getInt(3);
        if (isPlayerAlive) {
            PacketManager.sendItemPacketToPlayer(getOwnerUUID,key,id,x,y);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }
    
    public static MethodResult removeItemElement(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        String key = args.getString(0);
        if (isPlayerAlive) {
            PacketManager.removeItemElementPacket(getOwnerUUID,key);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }
    
    //IM EDGING TO CLEARING EVERYTHING

    public static MethodResult clearText(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        if (isPlayerAlive) {
            PacketManager.removeAllTextElementsPacket(getOwnerUUID);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

    public static MethodResult clearRightboundText(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        if (isPlayerAlive) {
            PacketManager.removeAllRightboundTextElementsPacket(getOwnerUUID);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

    public static MethodResult clearRect(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        if (isPlayerAlive) {
            PacketManager.removeAllRectangleElementsPacket(getOwnerUUID);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

    public static MethodResult clearItem(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        if (isPlayerAlive) {
            PacketManager.removeAllItemElementsPacket(getOwnerUUID);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

    public static MethodResult clearAll(IArguments args, Boolean isPlayerAlive, UUID getOwnerUUID) throws LuaException {
        if (isPlayerAlive) {
            PacketManager.removeAllTextElementsPacket(getOwnerUUID);
            PacketManager.removeAllRightboundTextElementsPacket(getOwnerUUID);
            PacketManager.removeAllRectangleElementsPacket(getOwnerUUID);
            PacketManager.removeAllItemElementsPacket(getOwnerUUID);
            return MethodResult.of(true);
        } else {
            return MethodResult.of(false);
        }
    }

}
