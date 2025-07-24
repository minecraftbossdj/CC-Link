package com.awesoft.cclink.item.LinkCore;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ServerComputer;

import dan200.computercraft.shared.network.server.ServerNetworking;


import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

import dan200.computercraft.shared.pocket.peripherals.PocketModemPeripheral;
import dan200.computercraft.shared.pocket.peripherals.PocketSpeakerPeripheral;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class LinkBrain implements IPocketAccess {
    private final LinkServerComputer computer;
    private LinkHolder holder;
    private Vec3 position;
    private boolean dirty = false;
    @Nullable
    private UpgradeData<IPocketUpgrade> upgrade;
    private int colour = -1;
    private int lightColour = -1;

    public LinkBrain(LinkHolder holder, @Nullable UpgradeData<IPocketUpgrade> upgrade, ServerComputer.Properties properties) {
        this.computer = new LinkServerComputer(this, holder, properties);
        this.holder = holder;
        this.position = holder.pos();
        this.upgrade = UpgradeData.copyOf(upgrade);
        this.invalidatePeripheral();
    }

    public LinkServerComputer computer() {
        return this.computer;
    }

    LinkHolder holder() {
        return this.holder;
    }

    public void addPeripheral(ComputerSide side, IPeripheral peripheral) {
        this.computer.setPeripheral(side,peripheral);
    }

    public void updateHolder(LinkHolder newHolder) {
        this.position = newHolder.pos();
        this.computer.setPosition(newHolder.level(), newHolder.blockPos());
        LinkHolder oldHolder = this.holder;
        if (!this.holder.equals(newHolder)) {
            this.holder = newHolder;
            ServerPlayer var10000;
            if (oldHolder instanceof LinkHolder.PlayerHolder) {
                LinkHolder.PlayerHolder p = (LinkHolder.PlayerHolder)oldHolder;
                var10000 = p.entity();
            } else if (oldHolder instanceof LinkHolder.PlayerCuriosHolder) {
                LinkHolder.PlayerCuriosHolder p = (LinkHolder.PlayerCuriosHolder)oldHolder;
                var10000 = p.entity();
            } else {
                var10000 = null;
            }

            /*
            ServerPlayer oldPlayer = var10000;
            if (newHolder instanceof LinkHolder.PlayerHolder) {
                LinkHolder.PlayerHolder player = (LinkHolder.PlayerHolder)newHolder;
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
            LinkCoreComputerItem.setUpgrade(stack, UpgradeData.copyOf(this.upgrade));
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
        LinkHolder var2 = this.holder;
        Entity var10000;
        if (var2 instanceof LinkHolder.EntityHolder entity) {
            if (this.holder.isValid(this.computer)) {
                var10000 = entity.entity();
                return var10000;
            }
        }

        if (var2 instanceof LinkHolder.PlayerCuriosHolder entity) {
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
