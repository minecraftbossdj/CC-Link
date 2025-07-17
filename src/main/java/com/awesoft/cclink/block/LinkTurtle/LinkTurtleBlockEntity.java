package com.awesoft.cclink.block.LinkTurtle;

import com.awesoft.cclink.Registration.CCLinkComponents;
import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.upgrades.TurtleModem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.IntSupplier;

public class LinkTurtleBlockEntity extends TurtleBlockEntity {


    public LinkTurtleBlockEntity(
            BlockEntityType<LinkTurtleBlockEntity> type,
            BlockPos pos, BlockState state,
            IntSupplier fuelLimit, ComputerFamily family
    ) {
        super(type, pos, state, fuelLimit, family);
    }

    @Override
    public ServerComputer createComputer(int id) {
        try {
            Field brainField = TurtleBlockEntity.class.getDeclaredField("brain");
            brainField.setAccessible(true);
            TurtleBrain brain = (TurtleBrain) brainField.get(this);

            ServerComputer computer = new ServerComputer((ServerLevel)this.getLevel(), this.getBlockPos(), ServerComputer.properties(id, this.getFamily()).label(this.getLabel())
                    .terminalSize(39, 13)
                    .addComponent(CCLinkComponents.TURTLE_LINK, brain)
                    .addComponent(ComputerComponents.TURTLE, brain)
            );
            brain.setupComputer(computer);

            return computer;
        } catch (Exception e) {
            e.printStackTrace();
        } //i hate this, but this lowkey works, wtf :sob:

        return super.createComputer(id);
    }


}