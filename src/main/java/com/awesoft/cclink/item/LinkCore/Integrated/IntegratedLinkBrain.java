package com.awesoft.cclink.item.LinkCore.Integrated;

import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkHolder;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.pocket.peripherals.PocketModemPeripheral;
import dan200.computercraft.shared.pocket.peripherals.PocketSpeakerPeripheral;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class IntegratedLinkBrain implements IPocketAccess {
    private final IntegratedLinkServerComputer computer;
    private IntegratedLinkHolder holder;
    private Vec3 position;
    private boolean dirty = false;
    @Nullable
    private UpgradeData<IPocketUpgrade> upgrade;
    private int colour = -1;
    private int lightColour = -1;

    public IntegratedLinkBrain(IntegratedLinkHolder holder, @Nullable UpgradeData<IPocketUpgrade> upgrade, ServerComputer.Properties properties) {
        this.computer = new IntegratedLinkServerComputer(this, holder, properties.terminalSize(Config.TURTLE_TERM_WIDTH,Config.TURTLE_TERM_HEIGHT));
        this.holder = holder;
        this.position = holder.pos();
        this.upgrade = UpgradeData.copyOf(upgrade);
        this.invalidatePeripheral();
        this.computer.setPeripheral(ComputerSide.TOP,new PocketModemPeripheral(false,this));
        this.computer.setPeripheral(ComputerSide.BOTTOM,new PocketSpeakerPeripheral(this));
    }

    public IntegratedLinkServerComputer computer() {
        return this.computer;
    }

    public int getSelectedSlot() {
        return 1;
    }

    public boolean canPlayerUse(Player player) {
        return true;
    }

    IntegratedLinkHolder holder() {
        return this.holder;
    }

    public void addPeripheral(ComputerSide side, IPeripheral peripheral) {
        this.computer.setPeripheral(side,peripheral);
    }

    public void updateHolder(IntegratedLinkHolder newHolder) {
        this.position = newHolder.pos();
        this.computer.setPosition(newHolder.level(), newHolder.blockPos());
        IntegratedLinkHolder oldHolder = this.holder;
        if (!this.holder.equals(newHolder)) {
            this.holder = newHolder;
            ServerPlayer var10000;
            if (oldHolder instanceof IntegratedLinkHolder.PlayerHolder) {
                IntegratedLinkHolder.PlayerHolder p = (IntegratedLinkHolder.PlayerHolder)oldHolder;
                var10000 = p.entity();
            } else if (oldHolder instanceof IntegratedLinkHolder.PlayerCuriosHolder) {
                IntegratedLinkHolder.PlayerCuriosHolder p = (IntegratedLinkHolder.PlayerCuriosHolder)oldHolder;
                var10000 = p.entity();
            } else {
                var10000 = null;
            }

            /*
            ServerPlayer oldPlayer = var10000;
            if (newHolder instanceof IntegratedLinkHolder.PlayerHolder) {
                IntegratedLinkHolder.PlayerHolder player = (IntegratedLinkHolder.PlayerHolder)newHolder;
                if (player.entity() != oldPlayer) {
                    ServerNetworking.sendToPlayer(new PocketComputerDataMessage(this.computer, true), player.entity());
                }
            }

             */

        }
    }

    public boolean updateItem(ItemStack stack) {
        if (!this.dirty) {
            return false;
        } else {
            this.dirty = false;
            IColouredItem.setColourBasic(stack, this.colour);
            IntegratedLinkCoreComputerItem.setUpgrade(stack, UpgradeData.copyOf(this.upgrade));
            return true;
        }
    }

    public ServerLevel getLevel() {
        return this.computer.getLevel();
    }

    public Vec3 getPosition() {
        return this.position;
    }

    @Nullable
    public Entity getEntity() {
        IntegratedLinkHolder var2 = this.holder;
        Entity var10000;
        if (var2 instanceof IntegratedLinkHolder.EntityHolder entity) {
            if (this.holder.isValid(this.computer)) {
                var10000 = entity.entity();
                return var10000;
            }
        }

        var10000 = null;
        return var10000;
    }

    public int getColour() {
        return this.colour;
    }

    public void setColour(int colour) {
        if (this.colour != colour) {
            this.dirty = true;
            this.colour = colour;
        }
    }

    public int getLight() {
        return this.lightColour;
    }

    public void setLight(int colour) {
        if (colour < 0 || colour > 16777215) {
            colour = -1;
        }

        this.lightColour = colour;
    }

    public CompoundTag getUpgradeNBTData() {
        UpgradeData<IPocketUpgrade> upgrade = this.upgrade;
        return upgrade == null ? new CompoundTag() : upgrade.data();
    }

    public void updateUpgradeNBTData() {
        this.dirty = true;
    }

    public void invalidatePeripheral() {
        IPeripheral peripheral = this.upgrade == null ? null : ((IPocketUpgrade)this.upgrade.upgrade()).createPeripheral(this);
        this.computer.setPeripheral(ComputerSide.BACK, peripheral);
    }

    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    public Map<ResourceLocation, IPeripheral> getUpgrades() {
        UpgradeData<IPocketUpgrade> upgrade = this.upgrade;
        return upgrade == null ? Map.of() : Collections.singletonMap(((IPocketUpgrade)upgrade.upgrade()).getUpgradeID(), this.computer.getPeripheral(ComputerSide.BACK));
    }

    @Nullable
    public UpgradeData<IPocketUpgrade> getUpgrade() {
        return this.upgrade;
    }

    public void setUpgrade(@Nullable UpgradeData<IPocketUpgrade> upgrade) {
        if (!Objects.equals(this.upgrade, upgrade)) {
            this.upgrade = UpgradeData.copyOf(upgrade);
            this.dirty = true;
            this.invalidatePeripheral();
        }
    }
}
