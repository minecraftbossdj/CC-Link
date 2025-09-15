package com.awesoft.cclink.item.EntityLink;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.hudoverlay.packets.HUDOverlayUpdatePacket;
import com.awesoft.cclink.hudoverlay.packets.PacketManager;
import com.awesoft.cclink.libs.LuaConverter;
import com.awesoft.cclink.libs.PacketCooldownManager;
import com.awesoft.cclink.libs.RaycastingUtil;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.pocket.IPocketAccess;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
public class EntityLinkAPI implements ILuaAPI {
    private static final Logger LOG = LoggerFactory.getLogger(EntityLinkAPI.class);

    private final IComputerSystem computer;
    private final IPocketAccess pocket;


    public EntityLinkAPI(IComputerSystem computer, IPocketAccess pocket) {
        this.computer = computer;
        this.pocket = pocket;
    }

    @Override
    public String[] getNames() {
        return new String[]{ "entity" };
    }

    private static Object createOutput(String output) {
        return new Object[]{ output };
    }

    public final Level getLevel() {
        return computer.getLevel();
    }

    public final LivingEntity getEntity() {
        if (pocket.getEntity() instanceof ServerPlayer plr) {
            ItemStack item = null;
            for (int i = 0; i < plr.getInventory().getContainerSize(); i++) {
                if (plr.getInventory().getItem(i).getTag().contains("entityUUID")) {
                    item = plr.getInventory().getItem(i);
                    break;
                }
            }

            if (item != null) {
                if (item.getTag().contains("entityUUID")) {
                    
                } else {
                    CCLink.LOGGER.info("i dont even got a funny quip for that one bro, just stop breaking the game");
                }
            }


        } else {
            return null;
        }
        return null;
    }

    public final Boolean isEntityAlive() {
        return getEntity().isAlive();
    }

    public final Boolean isEntityNotNull() {
        return getEntity() != null;
    }

    public final UUID getOwnerUUID() {
        if (isEntityNotNull()) {
            return getEntity().getUUID();
        }
        return null;
    }


    @LuaFunction(mainThread = true)
    public final Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();

        LivingEntity entity = getEntity();
        if (isEntityNotNull()) {
            info.put("name", entity.getName().toString());
            info.put("dimension", entity.level().dimension().location().toString());
            info.put("health", entity.getHealth());
            info.put("armor", entity.getArmorValue());
            info.put("speed", entity.getSpeed());
            info.put("yaw", entity.getViewYRot(1));
            info.put("pitch", entity.getViewXRot(1));
            info.put("isInWater", entity.isInWater());
        }

        return info;
    }



    @LuaFunction(mainThread = true)
    public final MethodResult lookAtBlock(double x, double y, double z) {
        if (isEntityNotNull()) {
            getEntity().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(x, y, z));
            return MethodResult.of(true);
        } else {return MethodResult.of(false,"Entity dead.");}

    }

    @LuaFunction(mainThread = true)
    public final MethodResult lookAt(double x, double y){
        if (isEntityNotNull()) {
            getEntity().setXRot((float)x);
            getEntity().setYHeadRot((float)y);
            return MethodResult.of(true);
        } else {return MethodResult.of(false,"Entity dead.");}
    }

    @LuaFunction(mainThread = true)
    public final MethodResult moveEntity(double x, double y, double z){
        if (isEntityNotNull()) {
            getEntity().push(x,y,z);
            return MethodResult.of(true);
        } else {return MethodResult.of(false,"Entity dead.");}
    }




}