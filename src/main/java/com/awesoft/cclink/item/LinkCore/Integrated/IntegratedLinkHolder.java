package com.awesoft.cclink.item.LinkCore.Integrated;

import com.awesoft.cclink.item.LinkCore.LinkHolder;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.lectern.CustomLecternBlockEntity;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;

public sealed interface IntegratedLinkHolder {
    ServerLevel level();

    Vec3 pos();

    BlockPos blockPos();

    boolean isValid(ServerComputer var1);

    boolean isCuriosValid(ServerComputer computer, ItemStack stack);

    void setChanged();

    default boolean isTerminalAlwaysVisible() {
        return false;
    }

    public sealed interface EntityHolder extends IntegratedLinkHolder permits IntegratedLinkHolder.PlayerHolder, IntegratedLinkHolder.ItemEntityHolder, IntegratedLinkHolder.PlayerCuriosHolder {
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

    public static record PlayerHolder(ServerPlayer entity, int slot) implements IntegratedLinkHolder.EntityHolder {
        public PlayerHolder(ServerPlayer entity, int slot) {
            this.entity = entity;
            this.slot = slot;
        }

        public boolean isValid(ServerComputer computer) {
            return this.entity().isAlive() && IntegratedLinkCoreComputerItem.isServerComputer(computer, this.entity().getInventory().getItem(this.slot()));
        }

        public boolean isCuriosValid(ServerComputer computer, ItemStack stack) {
            AtomicBoolean isCuriosFound = new AtomicBoolean(false);
            CuriosApi.getCuriosInventory(this.entity).ifPresent(curiosInv ->{
                if (curiosInv.findFirstCurio(stack.getItem()).isPresent()) {
                    isCuriosFound.set(true);
                }
            });
            return this.entity.isAlive() && isCuriosFound.get();
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

    public static record PlayerCuriosHolder(ServerPlayer entity) implements IntegratedLinkHolder.EntityHolder {
        public PlayerCuriosHolder(ServerPlayer entity) {
            this.entity = entity;
        }

        public boolean isValid(ServerComputer computer) {
            return this.entity().isAlive();
        }

        public boolean isCuriosValid(ServerComputer computer, ItemStack stack) {
            AtomicBoolean isCuriosFound = new AtomicBoolean(false);
            CuriosApi.getCuriosInventory(this.entity).ifPresent(curiosInv ->{
                if (curiosInv.findFirstCurio(stack.getItem()).isPresent()) {
                    isCuriosFound.set(true);
                }
            });
            return this.entity.isAlive() && isCuriosFound.get();
        }

        public void setChanged() {
            this.entity.getInventory().setChanged();
        }

        public ServerPlayer entity() {
            return this.entity;
        }
    }

    public static record ItemEntityHolder(
            ItemEntity entity) implements IntegratedLinkHolder.EntityHolder {
        public ItemEntityHolder(ItemEntity entity) {
            this.entity = entity;
        }

        public boolean isValid(ServerComputer computer) {
            return this.entity().isAlive() && IntegratedLinkCoreComputerItem.isServerComputer(computer, this.entity().getItem());
        }

        @Override
        public boolean isCuriosValid(ServerComputer computer, ItemStack stack) {
            return this.entity().isAlive() && IntegratedLinkCoreComputerItem.isServerComputer(computer, this.entity().getItem());
        }

        public void setChanged() {
            this.entity.setItem(this.entity.getItem().copy());
        }

        public ItemEntity entity() {
            return this.entity;
        }
    }

    public static record LecternHolder(
            CustomLecternBlockEntity lectern) implements IntegratedLinkHolder {
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
            return !this.lectern().isRemoved() && IntegratedLinkCoreComputerItem.isServerComputer(computer, this.lectern.getItem());
        }

        @Override
        public boolean isCuriosValid(ServerComputer computer, ItemStack stack) {
            return !this.lectern().isRemoved() && IntegratedLinkCoreComputerItem.isServerComputer(computer, this.lectern.getItem());
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
