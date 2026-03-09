package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.libs.ComputerLib;
import com.awesoft.cclink.registration.ItemRegistry;
import com.awesoft.cclink.upgrades.luaFunctions.base.UpgradeFunctionsBase;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChattyUpgradeFunctions extends UpgradeFunctionsBase {
    public Map<String, Object> functions = new HashMap<>();

    public ChattyUpgradeFunctions(Entity entity, boolean isIntegrated) {
        this.entity = entity;
        this.UPGRADE = ItemRegistry.CHATTY_UPGRADE.get();
        this.isIntegrated = isIntegrated;
    }

    public ILuaFunction sendMessage = args -> {
        if (!checkUpgrade()) return MethodResult.of(false, "Upgrade not equipped!");
        String msg = args.getString(0);
        String username = args.getString(1);
        String brackets = args.getString(2);

        entity.level().getServer().getPlayerList().getPlayers().forEach(player -> {
            player.sendSystemMessage(Component.literal(brackets.charAt(0)+username+brackets.charAt(1)+" "+msg));
        });
        return MethodResult.of(true);
    };

    public ILuaFunction sendMessageToPlayer = args -> {
        if (!checkUpgrade()) return MethodResult.of(false, "Upgrade not equipped!");
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
