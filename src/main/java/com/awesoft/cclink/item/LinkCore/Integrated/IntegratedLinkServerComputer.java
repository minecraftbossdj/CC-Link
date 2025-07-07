package com.awesoft.cclink.item.LinkCore.Integrated;


import com.awesoft.cclink.Registration.CCLinkComponents;
import com.awesoft.cclink.item.LinkCore.LinkHolder;
import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.config.ConfigSpec;
import dan200.computercraft.shared.network.client.PocketComputerDeletedClientMessage;
import dan200.computercraft.shared.network.server.ServerNetworking;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.Set;

public final class IntegratedLinkServerComputer extends ServerComputer {
    private final IntegratedLinkBrain brain;
    private int oldLightColour = -1;
    @Nullable
    private ComputerState oldComputerState;
    private Set<ServerPlayer> tracking = Set.of();

    IntegratedLinkServerComputer(IntegratedLinkBrain brain, IntegratedLinkHolder holder, Properties properties) {
        super(holder.level(), holder.blockPos(), properties
                .terminalSize((Integer)ConfigSpec.computerTermWidth.get(), (Integer)ConfigSpec.computerTermHeight.get())
                .addComponent(CCLinkComponents.INTEGRATED_LINK, brain)
                .addComponent(ComputerComponents.POCKET, brain)
        );

        this.brain = brain;
    }

    public IntegratedLinkBrain getBrain() {
        return this.brain;
    }

    public void addPeripheral(ComputerSide side, IPeripheral peripheral) {
        this.brain.addPeripheral(side,peripheral);
    }

    protected void onRemoved() {
        super.onRemoved();
        ServerNetworking.sendToAllPlayers(new PocketComputerDeletedClientMessage(this.getInstanceUUID()), this.getLevel().getServer());
    }

}

