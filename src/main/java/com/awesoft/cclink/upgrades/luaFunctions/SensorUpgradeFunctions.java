package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.libs.LuaConverter;
import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SensorUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;
    IComputerSystem computer;

    public SensorUpgradeFunctions(Entity entity, IComputerSystem computer) {
        this.entity = entity;
        this.computer = computer;
    }


    //I can neither confirm nor deny if I stole these from finerperipherals or not :clueless:
    private static final Map<Integer, Integer> lastCalledMap = new HashMap<>();
    private static final int cooldownTime = 2000;

    private int getRemainingCooldownTime(int computerId) {
        int lastCalled = lastCalledMap.getOrDefault(computerId, 0);
        int currentTime = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        int elapsed = currentTime - lastCalled;

        if (elapsed < 0) {
            elapsed += Integer.MAX_VALUE;
        }

        return Math.max(0, cooldownTime - elapsed);
    }

    private boolean canCallFunction(int computerId) {
        int remainingTime = getRemainingCooldownTime(computerId);
        return remainingTime <= 0;
    }

    public ILuaFunction sense = args -> {

        List<Object> entitiesList = new ArrayList<>(List.of());

        if (!canCallFunction(computer.getID())) {
            return MethodResult.of(null,"On cooldown!");

        }
        lastCalledMap.put(computer.getID(), (int) (System.currentTimeMillis() % Integer.MAX_VALUE)); //ok yes it MIGHT be universal but you're not NORMALLY gonna get 2 of the same ids so... who cares :clueless:

        if (args.getInt(0) > 16) return MethodResult.of(false,"Cannot be bigger than 16 block radius!"); //holy lazy
        int radius = args.getInt(0);

        Level level = entity.level();
        BlockPos pos = entity.getOnPos();

        AABB box = new AABB(
                pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
        );

        List<Entity> entities = level.getEntities(entity, box);

        for (Entity e : entities) {
            entitiesList.add(LuaConverter.completeEntityToLua(e,new ItemStack(Items.AIR)));
        }


        return MethodResult.of(entitiesList);

    };

    public ILuaFunction getOperationCooldown = args -> MethodResult.of(getRemainingCooldownTime(computer.getID()));

    public Map<String, Object> getFunctions() {
        functions.put("sense",sense);
        functions.put("getOperationCooldown",getOperationCooldown);
        return functions;
    }

}
