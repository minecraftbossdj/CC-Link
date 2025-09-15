package com.awesoft.cclink.item.LinkCore;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.hudoverlay.packets.HUDOverlayUpdatePacket;
import com.awesoft.cclink.hudoverlay.packets.PacketManager;
import com.awesoft.cclink.libs.ComputerLib;
import com.awesoft.cclink.libs.LuaConverter;
import com.awesoft.cclink.libs.PacketCooldownManager;
import com.awesoft.cclink.libs.RaycastingUtil;
import com.awesoft.cclink.upgrades.luaFunctions.*;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.shared.computer.blocks.ComputerPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.speaker.SpeakerPeripheral;
import dan200.computercraft.shared.pocket.peripherals.PocketModemPeripheral;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.pocket.IPocketAccess;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.awesoft.cclink.item.LinkCore.LinkCoreComputerItem.getServerComputer;

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

    public final Container getLinkInventory() {
        return ComputerLib.getContainerFromLinkHolder(getPlayer());
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
    public final MethodResult listUpgrades() {

        ArrayList<String> result = new ArrayList<>();
        var size = getLinkInventory().getContainerSize();
        for (var i = 0; i < size; i++) {
            var stack = getLinkInventory().getItem(i);
            if (!stack.isEmpty()) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                result.add(id.toString());
            }
        }
        return MethodResult.of(true, result);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult hasUpgrade(String upgradeid) {
        Item id = BuiltInRegistries.ITEM.get(new ResourceLocation(upgradeid));
        Set<Item> items = Set.of(id);

        return MethodResult.of(getLinkInventory().hasAnyOf(items));
    }

    public final boolean hasUpgradeNonLua(String upgradeid) {
        Item id = BuiltInRegistries.ITEM.get(new ResourceLocation(upgradeid));
        Set<Item> items = Set.of(id);

        return getLinkInventory().hasAnyOf(items);
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getUpgradeFunctions() {
        Map<String, Object> functions = new HashMap<>();

        if (hasUpgradeNonLua("cclink:scanner_upgrade")) {
            ScannerUpgradeFunctions funcs = new ScannerUpgradeFunctions(getPlayer(),computer);
            functions.put("scanner",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:sensor_upgrade")) {
            SensorUpgradeFunctions funcs = new SensorUpgradeFunctions(getPlayer(),computer);
            functions.put("sensor",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:introspection_upgrade")) {
            IntrospectionUpgradeFunctions funcs = new IntrospectionUpgradeFunctions(getPlayer());
            functions.put("introspection",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:world_upgrade")) {
            WorldUpgradeFunctions funcs = new WorldUpgradeFunctions(getPlayer());
            functions.put("world",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:overlay_upgrade")) {
            OverlayUpgradeFunctions funcs = new OverlayUpgradeFunctions(getPlayer());
            functions.put("overlay",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:kinetic_upgrade")) {
            KineticUpgradeFunctions funcs = new KineticUpgradeFunctions(getPlayer());
            functions.put("kinetic",funcs.getFunctions());
        }
        if (hasUpgradeNonLua("cclink:chatty_upgrade")) {
            ChattyUpgradeFunctions funcs = new ChattyUpgradeFunctions(getPlayer());
            functions.put("chatty",funcs.getFunctions());
        }
        //TODO: lasergun

        return functions;
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();

        Player plr = getPlayer();
        if (plr != null) {
            info.put("name", plr.getGameProfile().getName());
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
    public final MethodResult swingMainHand(){
        Player plr = getPlayer();
        if (plr != null) {
            plr.swing(InteractionHand.MAIN_HAND);
            return MethodResult.of(true);
        }
        else {return MethodResult.of(false,"player forgor");}
    }

    @LuaFunction(mainThread = true)
    public final boolean isClientside(){
        return getPlayer().level().isClientSide;
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> raycastBlock(double inputReach, boolean hitFluids) {

        double reach = Math.min(5,Math.max(1,inputReach));

        ClipContext.Fluid pFluidMode = ClipContext.Fluid.ANY;
        Player plr = getPlayer();
        Map<String, Object> info = new HashMap<>();
        if (plr != null) {
            Level lvl = plr.level();
            HitResult hitResult = plr.pick(5.0D, 0.0F, hitFluids);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hitResult;
                BlockPos pos = blockHit.getBlockPos();

                String name = String.valueOf(lvl.getBlockState(pos).getBlock().getName().toString());
                Block blk = lvl.getBlockState(pos).getBlock();

                info.put("id", ForgeRegistries.BLOCKS.getKey(blk).toString());
                info.put("name", new ItemStack(blk).getDisplayName().getString());
                info.put("x", pos.getX());
                info.put("y", pos.getY());
                info.put("z", pos.getZ());
            }
        }
        return info;
    }

    @Nullable
    @LuaFunction(mainThread = true)
    public final Map<String, Object> raycastEntity(double inputReach) {

        double reach = Math.min(5,Math.max(1,inputReach));

        Map<String, Object> info = new HashMap<>();
        Player plr = getPlayer();
        if (plr != null) {
            Optional<Entity> hitEntity = RaycastingUtil.raycastEntity(plr, reach);


            if (hitEntity.isPresent()) {
                Entity entity = hitEntity.get();


                info.put("id", ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
                info.put("uuid", entity.getUUID().toString());
                info.put("dimension", entity.level().dimension().location().toString());
                info.put("x", Math.floor(entity.getX()));
                info.put("y", Math.floor(entity.getY()));
                info.put("z", Math.floor(entity.getZ()));
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
    public final MethodResult consume(int slot){
        slot = slot - 1;
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
    public final MethodResult lookAtBlock(double x, double y, double z){
        getPlayer().lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(x,y,z));
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult lookAt(double x, double y){
        Player plr = getPlayer();
        if (plr != null) {
            plr.setXRot((float)x);
            plr.setYHeadRot((float)y);
            return MethodResult.of(true);
        }
        else {return MethodResult.of(false,"player forgor");}
    }


    @LuaFunction(mainThread = true)
    public final MethodResult list() throws LuaException {
        var inventory = getPlayer().getInventory();
        if (inventory == null) {
            throw new LuaException("Player isn't present");
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
    public final Map<String, Object> getSlot(int slot) throws LuaException {
        slot = slot - 1;
        return LuaConverter.stackToObject(getPlayer().getInventory().getItem(slot));
    }

    @LuaFunction(mainThread = true)
    public final boolean moveItem(int fromSlot, int count, int toSlot) throws LuaException {
        fromSlot = fromSlot - 1;
        toSlot = toSlot - 1;
        if (count > 0) {
            Inventory inv = getPlayer().getInventory();

            ItemStack fromSlotItem = inv.getItem(fromSlot);
            ItemStack toSlotItem = inv.getItem(toSlot);

            if (inv.contains(fromSlotItem)) {
                if (fromSlotItem.getCount() >= count) {
                    if (toSlotItem.getItem() == Items.AIR) {
                        ItemStack copiedItem = fromSlotItem.copy();
                        copiedItem.setCount(count);
                        inv.setItem(toSlot,copiedItem);
                        fromSlotItem.setCount(fromSlotItem.getCount()-count);
                        //CCLink.LOGGER.info("move "+fromSlotItem.getItem().asItem().toString()+" to new slot");
                        return true;
                    } else if (toSlotItem.getItem() == fromSlotItem.getItem()) {
                        if (toSlotItem.getCount()+count > toSlotItem.getMaxStackSize()) {
                            throw new LuaException("Cannot be above max stack size!");
                        } else {
                            toSlotItem.setCount(toSlotItem.getCount() + count);
                            fromSlotItem.setCount(fromSlotItem.getCount()-count);
                            //CCLink.LOGGER.info("move "+toSlotItem.getItem().asItem().toString()+" to a slot with a same item in it");
                            return true;
                        }
                    }
                }
            }



        } else {
            throw new LuaException("Count cant be below zero!");
        }

        return false;
    }


}