package com.awesoft.cclink.block.SecureComputer;

import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SecureComputerBlockEntity extends ComputerBlockEntity {
    public SecureComputerBlockEntity(BlockEntityType<? extends ComputerBlockEntity> type, BlockPos pos, BlockState state, ComputerFamily family) {
        super(type, pos, state, family);
    }
}
