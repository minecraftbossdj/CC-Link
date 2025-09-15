package com.awesoft.cclink.upgrades.luaFunctions;

import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ChattyUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;

    public ChattyUpgradeFunctions(Entity entity) {
        this.entity = entity;
    }

    public ILuaFunction sendMessage = args -> {
        String msg = args.getString(0);
        String username = args.getString(1);
        String brackets = args.getString(2);

        entity.level().getServer().getPlayerList().getPlayers().forEach(player -> {
            player.sendSystemMessage(Component.literal(brackets.charAt(0)+username+brackets.charAt(1)+" "+msg));
        });
        return MethodResult.of(true);
    };

    public ILuaFunction sendMessageToPlayer = args -> {
        String msg = args.getString(0);
        String plr = args.getString(1);
        String username = args.getString(2);
        String brackets = args.getString(3);

        if (entity.level().getServer().getPlayerList().getPlayerByName(plr) == null) { return MethodResult.of(false,"Player not online!");}

        entity.level().getServer().getPlayerList().getPlayerByName(plr).sendSystemMessage(Component.literal(brackets.charAt(0)+username+brackets.charAt(1)+" "+msg));

        return MethodResult.of(true);
    };

    public Map<String, Object> getFunctions() {
        functions.put("sendMessage",sendMessage);
        functions.put("sendMessageToPlayer",sendMessageToPlayer);
        return functions;
    }

}
