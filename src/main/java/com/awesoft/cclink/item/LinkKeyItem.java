package com.awesoft.cclink.item;

import com.awesoft.cclink.block.LinkTurtle.LinkTurtleBlockEntity;
import com.mojang.authlib.GameProfile;
import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class LinkKeyItem extends Item {
    public LinkKeyItem(Properties pProperties) {
        super(pProperties);
    }


    private static void openImpl(Player player, ItemStack stack, boolean isTypingOnly, ServerComputer computer) {
        PlatformHelper.get().openMenu(player, stack.getHoverName(), (id, inventory, entity) -> new ComputerMenuWithoutInventory((MenuType) ModRegistry.Menus.COMPUTER.get(), id, inventory, (p) -> true, computer), new ComputerContainerData(computer, stack));
    }

    private static int getSessionID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("SessionId") ? nbt.getInt("SessionId") : -1;
    }

    @Nullable
    public static UUID getInstanceID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.hasUUID("InstanceId") ? nbt.getUUID("InstanceId") : null;
    }

    @Nullable
    public static ServerComputer getServerComputer(ServerComputerRegistry registry, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        int sessionId = tag.getInt("SessionId");
        UUID instanceId = tag.getUUID("InstanceId");
        if (instanceId != null) {
            return registry.get(sessionId, instanceId);
        }
        return null;
    }

    @Nullable
    public static ServerComputer getServerComputer(MinecraftServer server, ItemStack stack) {
        if (server != null) {
            return getServerComputer(ServerContext.get(server).registry(), stack);
        } else {
            return null;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getPlayer().level().getBlockEntity(pContext.getClickedPos()) instanceof LinkTurtleBlockEntity) {
            if (pContext.getPlayer().getServer() != null) {
                MinecraftServer server = pContext.getPlayer().getServer();
                ServerComputerRegistry registry = ServerContext.get(server).registry();

                if (pContext.getPlayer().isCrouching()) {
                    BlockEntity blockEntity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());

                    if (blockEntity instanceof LinkTurtleBlockEntity tile) {
                        ServerComputer computer = tile.getServerComputer();
                        if (tile.getAccess().getOwningPlayer() == null) {return InteractionResult.FAIL;}
                        String ownerName = tile.getAccess().getOwningPlayer().getName();
                        if (ownerName.equals(pContext.getPlayer().getGameProfile().getName())) { //i COULD do uuids, however im not going to because then testing it in dev probably wont work. no wonder, its like the dev (probably) isnt a real account :WHAT:
                            if (computer != null) {
                                CompoundTag tag = pContext.getItemInHand().getOrCreateTag();
                                tag.putInt("SessionId", registry.getSessionID());
                                if (computer.getInstanceUUID() != null) {
                                    tag.putUUID("InstanceId", computer.getInstanceUUID());
                                } else {
                                    tag.putUUID("InstanceId", computer.register());
                                }
                                tag.putInt("computId", computer.getID());
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (pPlayer.isCrouching()) {
            HitResult hitResult = pPlayer.pick(5.0D, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.MISS) {
                CompoundTag tag = itemstack.getTag();
                if (tag != null) {
                    if (tag.contains("SessionId")) {
                        tag.remove("SessionId");
                    }
                    if (tag.contains("InstanceId")) {
                        tag.remove("InstanceId");
                    }
                    if (tag.contains("computId")) {
                        tag.remove("computId");
                    }
                }
                return InteractionResultHolder.success(itemstack);
            }
        } else {
            try {
                ServerComputer computer = getServerComputer(pPlayer.getServer(), itemstack);
                if (computer != null) {
                    if (!computer.isOn()) {
                        computer.turnOn();
                    }
                    openImpl(pPlayer, pPlayer.getItemInHand(pUsedHand), false, computer);
                    return InteractionResultHolder.success(itemstack);
                }
            } catch (RuntimeException ignored) {}
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }




    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);

        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (tag.contains("computId")) {
                tooltip.add(Component.literal("Linked to Id: " + tag.getInt("computId")));
            }
        } else {
            tooltip.add(Component.literal("Not Linked"));
        }
    }

}
