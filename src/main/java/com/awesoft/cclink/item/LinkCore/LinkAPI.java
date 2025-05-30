package com.awesoft.cclink.item.LinkCore;

import com.awesoft.cclink.hudoverlay.packets.PacketManager;
import com.awesoft.cclink.libs.LuaConverter;
import com.awesoft.cclink.libs.PacketCooldownManager;
import com.awesoft.cclink.libs.RaycastingUtil;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.pocket.IPocketAccess;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @cc.module commands
 * @cc.since 1.7
 */
public class LinkAPI implements ILuaAPI {
    private static final Logger LOG = LoggerFactory.getLogger(LinkAPI.class);

    private final IComputerSystem computer;
    private final IPocketAccess pocket;


    public LinkAPI(IComputerSystem computer, IPocketAccess pocket) {
        this.computer = computer;
        this.pocket = pocket;

    }

    @Override
    public String[] getNames() {
        return new String[]{ "link" };
    }

    private static Object createOutput(String output) {
        return new Object[]{ output };
    }

    public final Level getLevel() {
        return computer.getLevel();
    }

    public final ServerPlayer getPlayer() {

        if (pocket.getEntity() instanceof ServerPlayer player) {
            return player;
        } else {
            return null;
        }
    }

    public final Boolean isPlayerAlive() {
        return getPlayer() != null;
    }

    public final UUID getOwnerUUID() {
        if (getPlayer() != null) {
            return getPlayer().getUUID();
        }
        return null;
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getOverlay() {
        Map<String, Object> info = new HashMap<>();

        // oh my god its fucking text :WHAT:
        ILuaFunction addOrUpdateTextElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.addOrUpdateTextElement(args, isPlayerAlive(), getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("addOrUpdateTextElement",addOrUpdateTextElement);

        ILuaFunction removeTextElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.removeTextElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("removeTextElement",removeTextElement);

        //rightbound string :wilted_rose:
        ILuaFunction addOrUpdateRightboundTextElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.addOrUpdateRightboundTextElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("addOrUpdateRightboundTextElement",addOrUpdateRightboundTextElement);

        ILuaFunction removeRightboundTextElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.removeRightboundTextElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("removeRightboundTextElement",removeRightboundTextElement);

        //box elementer
        ILuaFunction addOrUpdateRectElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.addOrUpdateRectElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("addOrUpdateRectElement",addOrUpdateRectElement);

        ILuaFunction removeRectElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.removeRectElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("removeRectElement",removeRectElement);

        //item element
        ILuaFunction addOrUpdateItemElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.addOrUpdateItemElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("addOrUpdateItemElement",addOrUpdateItemElement);

        ILuaFunction removeItemElement = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.removeItemElement(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("removeItemElement",removeItemElement);


        //deletion all situation is CRAZY

        ILuaFunction clearText = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.clearText(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("clearText",clearText);

        ILuaFunction clearRightboundText = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.clearRightboundText(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("clearRightboundText",clearRightboundText);

        ILuaFunction clearRectangles = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.clearRect(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("clearRectangles",clearRectangles);

        ILuaFunction clearItems = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.clearItem(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("clearItems",clearItems);

        ILuaFunction clearAll = args -> {
            if (PacketCooldownManager.canSendPacket(getOwnerUUID())) {
                return OverlayMethods.clearAll(args,isPlayerAlive(),getOwnerUUID());
            } else {
                return MethodResult.of(new HashMap<>(), "Still in cooldown!");
            }
        };
        info.put("clearAll",clearAll);

        return info;
    }


    @LuaFunction(mainThread = true)
    public final Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();

        Player plr = getPlayer();
        if (plr != null) {
            info.put("name", plr.getGameProfile().getName());
            info.put("x", Math.floor(plr.getX()));
            info.put("y", Math.floor(plr.getY()));
            info.put("z", Math.floor(plr.getZ()));
            info.put("dimension", plr.level().dimension().location().toString());
            info.put("health", plr.getHealth());
            info.put("hunger", plr.getFoodData().getFoodLevel());
            info.put("saturation", plr.getFoodData().getSaturationLevel());
            info.put("air", plr.getAirSupply());
            info.put("armor", plr.getArmorValue());
            info.put("speed", plr.getSpeed());
            info.put("yaw", plr.getViewYRot(1));
            info.put("pitch", plr.getViewXRot(1));
            info.put("isInPowderedSnow", plr.isInPowderSnow);
            info.put("isInWater", plr.isInWater());
            info.put("isHungry", plr.getFoodData().needsFood());
        }

        return info;
    }

    @LuaFunction(mainThread = true)
    public MethodResult swingMainHand(){
        Player plr = getPlayer();
        if (plr != null) {
            plr.swing(InteractionHand.MAIN_HAND);
            return MethodResult.of(true);
        }
        else {return MethodResult.of(false,"player forgor");}
    }

    @LuaFunction(mainThread = true)
    public Map<String, Object> raycastBlk(double reach) {
        ClipContext.Fluid pFluidMode = ClipContext.Fluid.ANY;
        Player plr = getPlayer();
        Map<String, Object> info = new HashMap<>();
        if (plr != null) {
            Level lvl = plr.level();
            float f = plr.getXRot();
            float f1 = plr.getYRot();
            Vec3 vec3 = plr.getEyePosition();
            float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
            float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
            float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
            float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
            float f6 = f3 * f4;
            float f7 = f2 * f4;
            Vec3 vec31 = vec3.add((double) f6 * reach, (double) f5 * reach, (double) f7 * reach);
            BlockPos pos = lvl.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, plr)).getBlockPos();
            Minecraft mc = Minecraft.getInstance();

            String name = String.valueOf(lvl.getBlockState(pos).getBlock().getName().toString());
            Block blk = lvl.getBlockState(pos).getBlock();

            info.put("id", ForgeRegistries.BLOCKS.getKey(blk).toString());
            info.put("name", new ItemStack(blk).getDisplayName().getString());
            info.put("x", pos.getX());
            info.put("y", pos.getY());
            info.put("z", pos.getZ());
        }
        return info;
    }

    @Nullable
    @LuaFunction(mainThread = true)
    public Map<String, Object> raycastEntity(double reach) {
        Map<String, Object> info = new HashMap<>();
        Player plr = getPlayer();
        if (plr != null) {
            Optional<Entity> hitEntity = RaycastingUtil.raycastEntity(plr, reach);


            if (hitEntity.isPresent()) {
                Entity entity = hitEntity.get();


                info.put("id", ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
                info.put("uuid", entity.getUUID().toString());
                info.put("x", entity.getX());
                info.put("y", entity.getY());
                info.put("z", entity.getZ());
                info.put("dimension", entity.level().dimension().location().toString());
                info.put("yaw", entity.getViewYRot(1));
                info.put("pitch", entity.getViewXRot(1));
                if (entity instanceof LivingEntity livingEntity) {
                    info.put("health", livingEntity.getHealth());
                    info.put("maxHealth", livingEntity.getMaxHealth());
                    info.put("air", livingEntity.getAirSupply());
                    info.put("maxAir", livingEntity.getMaxAirSupply());
                    info.put("armor", livingEntity.getArmorValue());
                    info.put("speed", livingEntity.getSpeed());
                    info.put("isInPowderedSnow", livingEntity.isInPowderSnow);

                    if (livingEntity instanceof Player hitPlayer) {
                        info.put("hunger", hitPlayer.getFoodData().getFoodLevel());
                        info.put("saturation", hitPlayer.getFoodData().getSaturationLevel());
                        info.put("isHungry", hitPlayer.getFoodData().needsFood());
                        info.put("creative", hitPlayer.isCreative());
                        info.put("spectator", hitPlayer.isSpectator());
                    }
                }
                if (entity instanceof Player hitPlayer) {
                    info.put("name", hitPlayer.getGameProfile().getName().toString());
                } else {
                    info.put("name", entity.getDisplayName().getString());
                }
                return info;
            } else {
                return info;
            }
        } else {
            return info;
        }
    }


    @LuaFunction(mainThread = true)
    public MethodResult consume(int slot){
        Player plr = getPlayer();
        if (plr != null) {
            Inventory inv = plr.getInventory();
            if (inv.getItem(slot).isEdible()) {
                if (plr.getFoodData().needsFood()) {
                    plr.eat(plr.level(), inv.getItem(slot));
                    return MethodResult.of(true);
                } else {
                    return MethodResult.of(false, "Player isnt hungry!");
                }

            } else {
                return MethodResult.of(false, "Item isnt edible!");
            }
        }
        else {return MethodResult.of(false,"player forgor");}
    }

    @LuaFunction(mainThread = true)
    public MethodResult lookAtBlock(double x, double y, double z){
        getPlayer().lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(x,y,z));
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public MethodResult lookAt(float x, float y){
        Player plr = getPlayer();
        if (plr != null) {
            plr.setXRot(x);
            plr.setYHeadRot(y);
            return MethodResult.of(true);
        }
        else {return MethodResult.of(false,"player forgor");}
    }


    @LuaFunction(mainThread = true)
    public MethodResult list() throws LuaException {
        var inventory = getPlayer().getInventory();
        if (inventory == null) {
            throw new LuaException("Player isn't present");
        }
        List<Object> items = new ArrayList<>();
        var size = inventory.getContainerSize();
        for (var i = 0; i < size; i++) {
            var stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                items.add(LuaConverter.stackToObjectWithSlot(stack, i));
            }
        }
        return MethodResult.of(items);
    }


    @LuaFunction(mainThread = true)
    public Map<String, Object> getSlot(int slot) throws LuaException {
        return LuaConverter.stackToObject(getPlayer().getInventory().getItem(slot));

        //return InventoryUtil.moveItem(new PlayerInvWrapper(getPlayer().getInventory()), , ItemFilter.fromStack(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Item)))));
    }

    @LuaFunction(mainThread = true)
    public boolean moveItem(int fromSlot, int count, int toSlot) throws LuaException {
        if (count > 0) {
            Inventory inv = getPlayer().getInventory();

            ItemStack itemstk = inv.getItem(fromSlot);

            if (inv.contains(itemstk)) {
                if (inv.getItem(fromSlot).getCount() >= count) {
                    if (inv.getItem(toSlot).getItem() == Items.AIR) {
                        ItemStack fake = itemstk.copy();
                        fake.setCount(count);
                        inv.setItem(toSlot, fake);
                        inv.removeItem(fromSlot, count);
                        return true;
                    } else if (inv.getItem(toSlot).getItem() == inv.getItem(fromSlot).getItem()) {
                        int newCount = count + inv.getItem(toSlot).getCount();
                        if (newCount > inv.getItem(fromSlot).getMaxStackSize()) {
                            throw new LuaException("Cannot be above max stack size!");
                        } else {
                            itemstk.setCount(newCount);
                            inv.setItem(toSlot, itemstk);
                            inv.removeItem(fromSlot, count);
                            return true;
                        }
                    } else {
                        throw new LuaException("Item exists in destination slot!");
                    }
                } else {
                    throw new LuaException("Not enough items in inventory!");
                }
            } else {
                throw new LuaException("Item does not exist in inventory!");
            }
        } else {
            throw new LuaException("Count cant be below zero!");
        }

        //return InventoryUtil.moveItem(new PlayerInvWrapper(getPlayer().getInventory()), , ItemFilter.fromStack(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Item)))));
    }

}