package com.awesoft.cclink.item.EntityLink;

import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.lectern.CustomLecternBlockEntity;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;

public sealed interface EntityLinkHolder {
    ServerLevel level();

    Vec3 pos();

    BlockPos blockPos();

    boolean isValid(ServerComputer var1);

    void setChanged();

    default boolean isTerminalAlwaysVisible() {
        return false;
    }

    public sealed interface EntityHolder extends EntityLinkHolder permits PlayerHolder, ItemEntityHolder,LivingEntityHolder {
        Entity entity();

        default ServerLevel level() {
            return (ServerLevel)this.entity().level();
        }

        default Vec3 pos() {
            return this.entity().getEyePosition();
        }

        default BlockPos blockPos() {
            return this.entity().blockPosition();
        }
    }

    public static record PlayerHolder(ServerPlayer entity, int slot) implements EntityHolder {
        public PlayerHolder(ServerPlayer entity, int slot) {
            this.entity = entity;
            this.slot = slot;
        }

        public boolean isValid(ServerComputer computer) {
            return this.entity().isAlive() && EntityLinkComputerItem.isServerComputer(computer, this.entity().getInventory().getItem(this.slot()));
        }

        public void setChanged() {
            this.entity.getInventory().setChanged();
        }

        public ServerPlayer entity() {
            return this.entity;
        }

        public int slot() {
            return this.slot;
        }
    }

    public static record LivingEntityHolder(LivingEntity entity, ServerPlayer player, int slot) implements EntityHolder {
        public LivingEntityHolder(LivingEntity entity, ServerPlayer player, int slot) {
            this.entity = entity;
            this.player = player;
            this.slot = slot;
        }

        public boolean isValid(ServerComputer computer) {
            return this.entity().isAlive();
        }

        public void setChanged() {
            this.player.getInventory().setChanged();
        }

        public LivingEntity entity() {
            return this.entity;
        }

        public int slot() {
            return this.slot;
        }
    }

    public static record ItemEntityHolder(
            ItemEntity entity) implements EntityHolder {
        public ItemEntityHolder(ItemEntity entity) {
            this.entity = entity;
        }

        public boolean isValid(ServerComputer computer) {
            return this.entity().isAlive() && EntityLinkComputerItem.isServerComputer(computer, this.entity().getItem());
        }

        public void setChanged() {
            this.entity.setItem(this.entity.getItem().copy());
        }

        public ItemEntity entity() {
            return this.entity;
        }
    }

    public static record LecternHolder(
            CustomLecternBlockEntity lectern) implements EntityLinkHolder {
        public LecternHolder(CustomLecternBlockEntity lectern) {
            this.lectern = lectern;
        }

        public ServerLevel level() {
            return (ServerLevel)this.lectern.getLevel();
        }

        public Vec3 pos() {
            return Vec3.atCenterOf(this.lectern.getBlockPos());
        }

        public BlockPos blockPos() {
            return this.lectern.getBlockPos();
        }

        public boolean isValid(ServerComputer computer) {
            return !this.lectern().isRemoved() && EntityLinkComputerItem.isServerComputer(computer, this.lectern.getItem());
        }

        public void setChanged() {
            BlockEntityHelpers.updateBlock(this.lectern());
        }

        public boolean isTerminalAlwaysVisible() {
            return true;
        }

        public CustomLecternBlockEntity lectern() {
            return this.lectern;
        }
    }
}
