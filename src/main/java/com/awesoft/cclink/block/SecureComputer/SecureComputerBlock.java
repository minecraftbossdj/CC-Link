package com.awesoft.cclink.block.SecureComputer;

import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import dan200.computercraft.shared.computer.blocks.ComputerBlock;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.platform.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SecureComputerBlock extends ComputerBlock {
    public SecureComputerBlock(Properties settings, RegistryEntry type) {
        super(settings, type);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isCrouching()) {
            BlockEntity serverComputer = level.getBlockEntity(pos);
            if (serverComputer instanceof AbstractComputerBlockEntity) {
                AbstractComputerBlockEntity computer = (AbstractComputerBlockEntity)serverComputer;
                if (!level.isClientSide && computer.isUsable(player)) {
                    ServerComputer ServerComputer = computer.createServerComputer();
                    ServerComputer.turnOn();
                    //PlatformHelper.get().openMenu(player, computer.getName(), computer, new ComputerContainerData(ServerComputer, this.getItem(computer)));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }

}
