package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.libs.ItemHandlerContainer;
import com.awesoft.cclink.libs.LuaConverter;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IntrospectionUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;

    public IntrospectionUpgradeFunctions(Entity entity) {
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

    public MethodResult moveItemTo(Container from, Container to, int fromSlot, int limit, int toSlot) {
        ItemStack fromItem = from.getItem(Math.max(0,fromSlot - 1));
        ItemStack toItem = to.getItem(Math.max(0,toSlot - 1));

        if (fromItem.isEmpty()) {
            return MethodResult.of(false, "From slot is empty!");
        }

        if (toItem.isEmpty()) {
            if (fromItem.getCount() < limit) {
                return MethodResult.of(false, "From slot has too few items!");
            }

            ItemStack moved = fromItem.copy();
            moved.setCount(limit);

            fromItem.shrink(limit);
            from.setItem(Math.max(0,fromSlot - 1), fromItem);
            to.setItem(Math.max(0,toSlot - 1), moved);

            return MethodResult.of(true, limit);
        }

        if (toItem.getItem() != fromItem.getItem()) {
            return MethodResult.of(false, "To slot doesn't contain the same item!");
        }

        if (!Objects.equals(toItem.getTag(), fromItem.getTag())) {
            return MethodResult.of(false, "Both items' NBT do not match.");
        }

        int transferable = Math.min(limit, fromItem.getCount());
        int space = toItem.getMaxStackSize() - toItem.getCount();
        int movedAmount = Math.min(transferable, space);

        fromItem.shrink(movedAmount);
        toItem.grow(movedAmount);

        from.setItem(Math.max(0,fromSlot - 1), fromItem);
        to.setItem(Math.max(0,toSlot - 1), toItem);

        return MethodResult.of(true, movedAmount);
    }

    //lua functions
    public ILuaFunction list = args -> {
        var inv = getInventory();
        var ender = getEnderInventory();
        if (inv == null || ender == null) {return MethodResult.of(false,"Player doesnt exist!");}

        Container contain1 = null;

        if (args.getString(0).equalsIgnoreCase("inv")) {contain1 = inv;}

        if (args.getString(0).equalsIgnoreCase("ender")) {contain1 = ender;}

        if (contain1 == null) {return MethodResult.of(false,"Invalid Inventory type! Please use \"inv\" or \"ender\"!");}

        Map<Integer, Map<String, ?>> result = new HashMap<>();
        var size = contain1.getContainerSize();
        for (var i = 0; i < size; i++) {
            var stack = contain1.getItem(i);
            if (!stack.isEmpty()) result.put(i + 1, VanillaDetailRegistries.ITEM_STACK.getBasicDetails(stack));
        }
        return MethodResult.of(result);
    };

    public ILuaFunction moveItems = args -> {
        var inv = getInventory();
        var ender = getEnderInventory();
        if (inv == null || ender == null) {return MethodResult.of(false,"Player doesnt exist!");}

        Container contain1 = null;
        Container contain2 = null;

        if (args.getString(0).equalsIgnoreCase("inv")) {contain1 = inv;}
        if (args.getString(1).equalsIgnoreCase("inv")) {contain2 = inv;}

        if (args.getString(0).equalsIgnoreCase("ender")) {contain1 = ender;}
        if (args.getString(1).equalsIgnoreCase("ender")) {contain2 = ender;}

        if (contain1 == null || contain2 == null) {return MethodResult.of(false,"Invalid Inventories! Please use \"inv\" or \"ender\"!");}

        int fromSlot = args.getInt(2);
        int limit = args.getInt(3);
        int toSlot = args.getInt(4);

        MethodResult result = moveItemTo(contain1,contain2,fromSlot,limit,toSlot);

        return MethodResult.of(result);
    };

    public ILuaFunction getItemDetail = args -> {
        var inv = getInventory();
        var ender = getEnderInventory();
        if (inv == null || ender == null) {return MethodResult.of(false,"Player doesnt exist!");}

        Container contain1 = null;

        if (args.getString(1).equalsIgnoreCase("inv")) {contain1 = inv;}

        if (args.getString(1).equalsIgnoreCase("ender")) {contain1 = ender;}

        if (contain1 == null) {return MethodResult.of(false,"Invalid Inventory type! Please use \"inv\" or \"ender\"!");}

        int slotNum = Math.max(0,Math.min(contain1.getContainerSize(),args.getInt(0)-1));

        return MethodResult.of(VanillaDetailRegistries.ITEM_STACK.getBasicDetails(contain1.getItem(slotNum)));
    };

    public ILuaFunction getItemLimit = args -> {
        var inv = getInventory();
        var ender = getEnderInventory();

        if (inv == null || ender == null) {return MethodResult.of(false,"Player doesnt exist!");}

        Container contain1 = null;

        if (args.getString(1).equalsIgnoreCase("inv")) {contain1 = inv;}

        if (args.getString(1).equalsIgnoreCase("ender")) {contain1 = ender;}

        if (contain1 == null) {return MethodResult.of(false,"Invalid Inventory type! Please use \"inv\" or \"ender\"!");}

        int slotNum = Math.max(0,Math.min(contain1.getContainerSize(),args.getInt(0)-1));

        return MethodResult.of(contain1.getItem(slotNum).getMaxStackSize());
    };

    public ILuaFunction size = args -> {
        var inv = getInventory();
        var ender = getEnderInventory();

        if (inv == null || ender == null) {return MethodResult.of(false,"Player doesnt exist!");}

        Container contain1 = null;

        if (args.getString(0).equalsIgnoreCase("inv")) {contain1 = inv;}

        if (args.getString(0).equalsIgnoreCase("ender")) {contain1 = ender;}

        if (contain1 == null) {return MethodResult.of(false, "Invalid Inventory type! Please use 'inv', 'ender' or 'curios'!");}

        return MethodResult.of(contain1.getContainerSize());
    };

    public Map<String, Object> getFunctions() {
        functions.put("list",list);
        functions.put("moveItems",moveItems);
        functions.put("getItemDetail",getItemDetail);
        functions.put("getItemLimit",getItemLimit);
        functions.put("size",size);
        return functions;
    }

}
