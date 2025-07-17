package com.awesoft.cclink.block.LinkTurtle;


import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.platform.RegistryEntry;
import dan200.computercraft.shared.turtle.blocks.TurtleBlock;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import dan200.computercraft.shared.util.WaterloggableHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LinkTurtleBlock extends TurtleBlock {

    @SuppressWarnings("unchecked")
    public LinkTurtleBlock(BlockBehaviour.Properties settings, RegistryEntry<? extends BlockEntityType<? extends TurtleBlockEntity>> type) {
        super(settings, (RegistryEntry<BlockEntityType<TurtleBlockEntity>>) type);
    }

    @Override
    @Deprecated
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var currentItem = player.getItemInHand(hand);
        if (currentItem.getItem() == Items.NAME_TAG && currentItem.hasCustomHoverName() && level.getBlockEntity(pos) instanceof AbstractComputerBlockEntity computer) {
            // Label to rename computer
            if (!level.isClientSide) {
                computer.setLabel(currentItem.getHoverName().getString());
                currentItem.shrink(1);
            }


            return InteractionResult.sidedSuccess(level.isClientSide);

        }
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof AbstractComputerBlockEntity computer && computer.isUsable(player)) {
                ServerComputer serverComputer = computer.createServerComputer();
                if (!serverComputer.isOn()) {
                    serverComputer.turnOn();
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }


}