package com.awesoft.cclink.item.LinkCore;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.Registration.ItemRegistry;
import com.awesoft.cclink.gui.LinkMenu;
import com.awesoft.cclink.libs.ItemHandlerContainer;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.Mount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.impl.PocketUpgrades;
import dan200.computercraft.shared.ModRegistry.Menus;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;
import dan200.computercraft.shared.computer.items.IComputerItem;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.container.SingleContainerData;
import dan200.computercraft.shared.lectern.CustomLecternBlock;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.NBTUtil;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class LinkCoreComputerItem extends Item implements IComputerItem, IMedia, IColouredItem, ICurioItem {
    private static final String NBT_UPGRADE = "Upgrade";
    private static final String NBT_UPGRADE_INFO = "UpgradeInfo";
    public static final String NBT_ON = "On";
    private static final String NBT_INSTANCE = "InstanceId";
    private static final String NBT_SESSION = "SessionId";
    private final ComputerFamily family;

    public LinkCoreComputerItem(Item.Properties settings, ComputerFamily family) {
        super(settings);
        this.family = family;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemInvLinkProvider(stack,5);
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = super.getShareTag(stack);
        if (tag == null) tag = new CompoundTag();

        CompoundTag finalTag = tag;
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> {
            if (cap instanceof ItemStackHandler invProvider) {
                finalTag.put("Inventory", invProvider.serializeNBT());
            }
        });

        return finalTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null && nbt.contains("Inventory")) {
            stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> {
                if (cap instanceof ItemStackHandler handler) {
                    handler.deserializeNBT(nbt.getCompound("Inventory"));
                }
            });
        }
    }




    public ItemStack create(int id, @Nullable String label, int colour, @Nullable UpgradeData<IPocketUpgrade> upgrade) {
        ItemStack result = new ItemStack(this);
        if (id >= 0) {
            result.getOrCreateTag().putInt("ComputerId", id);
        }

        if (label != null) {
            result.setHoverName(Component.literal(label));
        }

        if (upgrade != null) {
            result.getOrCreateTag().putString("Upgrade", ((IPocketUpgrade)upgrade.upgrade()).getUpgradeID().toString());
            if (!upgrade.data().isEmpty()) {
                result.getOrCreateTag().put("UpgradeInfo", upgrade.data().copy());
            }
        }

        if (colour != -1) {
            result.getOrCreateTag().putInt("Color", colour);
        }

        return result;
    }

    public void tick(ItemStack stack, LinkHolder holder, boolean passive) {
        LinkBrain brain;
        if (passive) {
            LinkServerComputer computer = getServerComputer(holder.level().getServer(), stack);
            if (computer == null) {
                return;
            }

            brain = computer.getBrain();
        } else {
            brain = this.getOrCreateBrain(holder.level(), holder, stack);
            brain.computer().keepAlive();
        }

        UpgradeData<IPocketUpgrade> upgrade = brain.getUpgrade();
        if (upgrade != null) {
            ((IPocketUpgrade)upgrade.upgrade()).update(brain, brain.computer().getPeripheral(ComputerSide.BACK));
        }

        if (this.updateItem(stack, brain)) {
            holder.setChanged();
        }

    }

    private boolean updateItem(ItemStack stack, LinkBrain brain) {
        boolean changed = brain.updateItem(stack);
        LinkServerComputer computer = brain.computer();
        int id = computer.getID();
        if (id != this.getComputerID(stack)) {
            changed = true;
            setComputerID(stack, id);
        }

        String label = computer.getLabel();
        if (!Objects.equals(label, this.getLabel(stack))) {
            changed = true;
            this.setLabel(stack, label);
        }

        boolean on = computer.isOn();
        if (on != isMarkedOn(stack)) {
            changed = true;
            stack.getOrCreateTag().putBoolean("On", on);
        }

        return changed;
    }

    public final boolean hasUpgrade(String upgradeid, ItemStack stack) {
        Item id = BuiltInRegistries.ITEM.get(new ResourceLocation(upgradeid));
        Set<Item> items = Set.of(id);

        Container Inv = new ItemHandlerContainer(stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null));

        return Inv.hasAnyOf(items);
    }

    public void inventoryTick(ItemStack stack, Level world, Entity entity, int compartmentSlot, boolean selected) {
        if (!world.isClientSide && entity instanceof ServerPlayer player) {
            if (compartmentSlot <= 0) {
                this.tick(stack, new LinkHolder.PlayerCuriosHolder(player), false);
            } else {
                int slot = InventoryUtil.getInventorySlotFromCompartment(player, compartmentSlot, stack);
                if (slot >= 0) {
                    this.tick(stack, new LinkHolder.PlayerHolder(player, slot), false);
                }
            }

            ServerComputer computer = getServerComputer(world.getServer(),stack);

        }
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        if (!level.isClientSide && level.getServer() != null) {
            this.tick(stack, new LinkHolder.ItemEntityHolder(entity), true);
            return false;
        } else {
            return false;
        }
    }

    public InteractionResult useOn(UseOnContext context) {
        return CustomLecternBlock.defaultUseItemOn(context);
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!world.isClientSide) {
            if (helmet.getItem() == ItemRegistry.LINK_INTERFACE.get()) {
                LinkHolder.PlayerHolder holder = new LinkHolder.PlayerHolder((ServerPlayer) player, InventoryUtil.getHandSlot(player, hand));
                LinkBrain brain = this.getOrCreateBrain((ServerLevel) world, holder, stack);
                LinkServerComputer computer = brain.computer();
                computer.turnOn();
                boolean stop = false;
                IPocketUpgrade upgrade = getUpgrade(stack);
                if (upgrade != null) {
                    stop = upgrade.onRightClick(world, brain, computer.getPeripheral(ComputerSide.BACK));
                    this.updateItem(stack, brain);
                }

                if (!stop) {
                    //openImpl(player, stack, holder, hand == InteractionHand.OFF_HAND, computer);

                    PlatformHelper.get().openMenu(
                            player,
                            player.getItemInHand(hand).getHoverName(),
                            (id, inventory, entity) ->
                                    new LinkMenu(
                                            id,
                                            (p) -> true,
                                            ComputerFamily.ADVANCED,
                                            computer,
                                            null,
                                            player.getInventory(),
                                            new ItemHandlerContainer(stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null)),
                                            (SingleContainerData) brain::getSelectedSlot

                                    ),
                            new ComputerContainerData(computer, ItemStack.EMPTY)
                    );
                }
            } else {
                CuriosApi.getCuriosHelper().findFirstCurio(player, ItemRegistry.LINK_INTERFACE.get()).ifPresent((slotResult) -> {
                    LinkHolder.PlayerHolder holder = new LinkHolder.PlayerHolder((ServerPlayer) player, InventoryUtil.getHandSlot(player, hand));
                    LinkBrain brain = this.getOrCreateBrain((ServerLevel) world, holder, stack);
                    LinkServerComputer computer = brain.computer();
                    computer.turnOn();
                    boolean stop = false;
                    IPocketUpgrade upgrade = getUpgrade(stack);
                    if (upgrade != null) {
                        stop = upgrade.onRightClick(world, brain, computer.getPeripheral(ComputerSide.BACK));
                        this.updateItem(stack, brain);
                    }

                    if (!stop) {
                        //openImpl(player, stack, holder, hand == InteractionHand.OFF_HAND, computer);
                        PlatformHelper.get().openMenu(
                                player,
                                player.getItemInHand(hand).getHoverName(),
                                (id, inventory, entity) ->
                                        new LinkMenu(
                                                id,
                                                (p) -> true,
                                                ComputerFamily.ADVANCED,
                                                computer,
                                                null,
                                                player.getInventory(),
                                                new ItemHandlerContainer(stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null)),
                                                (SingleContainerData) brain::getSelectedSlot

                                        ),
                                new ComputerContainerData(computer, ItemStack.EMPTY)
                        );

                    }
                });
            }
        }

        return new InteractionResultHolder(InteractionResult.sidedSuccess(world.isClientSide), stack);
    }

    public void open(Player player, ItemStack stack, LinkHolder holder, boolean isTypingOnly) {
        LinkBrain brain = this.getOrCreateBrain(holder.level(), holder, stack);
        LinkServerComputer computer = brain.computer();
        computer.turnOn();
        openImpl(player, stack, holder, isTypingOnly, computer);
    }

    //the thing thats fucking over everything
    private static void openImpl(Player player, ItemStack stack, LinkHolder holder, boolean isTypingOnly, ServerComputer computer) {
        PlatformHelper.get().openMenu(player, stack.getHoverName(), (id, inventory, entity) -> new ComputerMenuWithoutInventory(isTypingOnly ? (MenuType)Menus.POCKET_COMPUTER_NO_TERM.get() : (MenuType)Menus.COMPUTER.get(), id, inventory, (p) -> holder.isValid(computer), computer), new ComputerContainerData(computer, stack));
    }

    public Component getName(ItemStack stack) {
        String baseString = this.getDescriptionId(stack);
        IPocketUpgrade upgrade = getUpgrade(stack);
        return (Component)(upgrade != null ? Component.translatable(baseString + ".upgraded", new Object[]{Component.translatable(upgrade.getUnlocalisedAdjective())}) : super.getName(stack));
    }

    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        if (flag.isAdvanced() || this.getLabel(stack) == null) {
            int id = this.getComputerID(stack);
            if (id >= 0) {
                list.add(Component.translatable("gui.computercraft.tooltip.computer_id", new Object[]{id}).withStyle(ChatFormatting.GRAY));
            }
        }

    }

    @Nullable
    public String getCreatorModId(ItemStack stack) {
        IPocketUpgrade upgrade = getUpgrade(stack);
        if (upgrade != null) {
            String mod = PocketUpgrades.instance().getOwner(upgrade);
            if (mod != null && !mod.equals(CCLink.MODID)) {
                return mod;
            }
        }

        return CCLink.MODID;
    }

    private LinkBrain getOrCreateBrain(ServerLevel level, LinkHolder holder, ItemStack stack) {
        ServerComputerRegistry registry = ServerContext.get(level.getServer()).registry();
        LinkServerComputer computer = getServerComputer(registry, stack);
        if (computer != null) {
            LinkBrain brain = computer.getBrain();
            brain.updateHolder(holder);
            return brain;
        } else {
            int computerID = this.getComputerID(stack);
            if (computerID < 0) {
                computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(level.getServer(), "computer");
                setComputerID(stack, computerID);
            }

            LinkBrain brain = new LinkBrain(holder, getUpgradeWithData(stack), ServerComputer.properties(this.getComputerID(stack), this.getFamily()).label(this.getLabel(stack)));
            LinkServerComputer computerSigma = brain.computer();
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("SessionId", registry.getSessionID());
            tag.putUUID("InstanceId", computerSigma.register());
            if (isMarkedOn(stack)) {
                computerSigma.turnOn();
            }

            this.updateItem(stack, brain);
            holder.setChanged();
            return brain;
        }
    }

    public static boolean isServerComputer(ServerComputer computer, ItemStack stack) {
        return stack.getItem() instanceof LinkCoreComputerItem && getServerComputer(computer.getLevel().getServer(), stack) == computer;
    }

    @Nullable
    public static LinkServerComputer getServerComputer(ServerComputerRegistry registry, ItemStack stack) {
        return (LinkServerComputer)registry.get(getSessionID(stack), getInstanceID(stack));
    }

    @Nullable
    public static LinkServerComputer getServerComputer(MinecraftServer server, ItemStack stack) {
        return getServerComputer(ServerContext.get(server).registry(), stack);
    }

    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            MinecraftServer server = level.getServer();
            if (server != null) {
                LinkServerComputer computer = getServerComputer(server, stack);
                if (computer != null) {
                    computer.getBrain().setUpgrade(getUpgradeWithData(stack));
                }
            }

        }
    }

    private static void setComputerID(ItemStack stack, int computerID) {
        stack.getOrCreateTag().putInt("ComputerId", computerID);
    }

    @Nullable
    public String getLabel(ItemStack stack) {
        return IComputerItem.super.getLabel(stack);
    }

    public ComputerFamily getFamily() {
        return this.family;
    }

    public ItemStack changeItem(ItemStack stack, Item newItem) {
        ItemStack var10000;
        if (newItem instanceof LinkCoreComputerItem pocket) {
            var10000 = pocket.create(this.getComputerID(stack), this.getLabel(stack), this.getColour(stack), getUpgradeWithData(stack));
        } else {
            var10000 = ItemStack.EMPTY;
        }

        return var10000;
    }

    public boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label != null) {
            stack.setHoverName(Component.literal(label));
        } else {
            stack.resetHoverName();
        }

        return true;
    }

    @Nullable
    public Mount createDataMount(ItemStack stack, ServerLevel level) {
        int id = this.getComputerID(stack);
        return id >= 0 ? ComputerCraftAPI.createSaveDirMount(level.getServer(), "computer/" + id, (long)Config.computerSpaceLimit) : null;
    }

    @Nullable
    public static UUID getInstanceID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.hasUUID("InstanceId") ? nbt.getUUID("InstanceId") : null;
    }

    private static int getSessionID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("SessionId") ? nbt.getInt("SessionId") : -1;
    }

    private static boolean isMarkedOn(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.getBoolean("On");
    }

    @Nullable
    public static IPocketUpgrade getUpgrade(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        return compound != null && compound.contains("Upgrade") ? (IPocketUpgrade)PocketUpgrades.instance().get(compound.getString("Upgrade")) : null;
    }

    @Nullable
    public static UpgradeData<IPocketUpgrade> getUpgradeWithData(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound != null && compound.contains("Upgrade")) {
            IPocketUpgrade upgrade = (IPocketUpgrade)PocketUpgrades.instance().get(compound.getString("Upgrade"));
            return upgrade == null ? null : UpgradeData.of(upgrade, NBTUtil.getCompoundOrEmpty(compound, "UpgradeInfo"));
        } else {
            return null;
        }
    }

    public static void setUpgrade(ItemStack stack, @Nullable UpgradeData<IPocketUpgrade> upgrade) {
        CompoundTag compound = stack.getOrCreateTag();
        if (upgrade == null) {
            compound.remove("Upgrade");
            compound.remove("UpgradeInfo");
        } else {
            compound.putString("Upgrade", ((IPocketUpgrade)upgrade.upgrade()).getUpgradeID().toString());
            compound.put("UpgradeInfo", upgrade.data().copy());
        }

    }

    public static CompoundTag getUpgradeInfo(ItemStack stack) {
        return stack.getOrCreateTagElement("UpgradeInfo");
    }
}
