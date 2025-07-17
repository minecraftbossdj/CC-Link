package com.awesoft.cclink.block.LinkTurtle;

import com.awesoft.cclink.item.LinkCore.OverlayMethods;
import com.awesoft.cclink.libs.LuaConverter;
import com.awesoft.cclink.libs.PacketCooldownManager;
import com.awesoft.cclink.libs.RaycastingUtil;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @cc.module commands
 * @cc.since 1.7
 */


public class TurtleLinkAPI implements ILuaAPI {
    private static final Logger LOG = LoggerFactory.getLogger(TurtleLinkAPI.class);

    private final IComputerSystem computer;
    private final ITurtleAccess turtle;


    public TurtleLinkAPI(IComputerSystem computer, ITurtleAccess turtle) {
        this.computer = computer;
        this.turtle = turtle;

    }

    @Override
    public String[] getNames() {
        return new String[]{ "link_turtle" };
    }

    private static Object createOutput(String output) {
        return new Object[]{ output };
    }

    @LuaFunction(mainThread = true)
    public Map<String, Object> raycast(double reach) {
        return RaycastingUtil.raycast(reach,turtle,turtle.getDirection());
    }

    @LuaFunction(mainThread = true)
    public Map<String, Object> raycastUp(double reach) {
        return RaycastingUtil.raycast(reach,turtle,Direction.UP);
    }

    @LuaFunction(mainThread = true)
    public Map<String, Object> raycastDown(double reach) {
        return RaycastingUtil.raycast(reach,turtle,Direction.DOWN);
    }


    @LuaFunction(mainThread = true)
    public MethodResult list() throws LuaException {
        var inventory = turtle.getInventory();
        if (inventory == null) {
            throw new LuaException("Turtle isn't present");
        }
        List<Object> items = new ArrayList<>();
        var size = inventory.getContainerSize();
        for (var i = 0; i < size; i++) {
            var stack = inventory.getItem(i);
            items.add(LuaConverter.stackToObjectWithSlot(stack, i));
        }
        return MethodResult.of(items);
    }

    @LuaFunction(mainThread = true)
    public MethodResult getDirection() {
        return MethodResult.of(turtle.getDirection().toString());
    }


}