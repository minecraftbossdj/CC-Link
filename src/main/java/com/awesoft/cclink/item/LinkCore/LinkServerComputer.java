package com.awesoft.cclink.item.LinkCore;


import com.awesoft.cclink.Registration.CCLinkComponents;
import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.config.ConfigSpec;
import dan200.computercraft.shared.network.client.PocketComputerDeletedClientMessage;
import dan200.computercraft.shared.network.server.ServerNetworking;

import java.util.Set;
import javax.annotation.Nullable;


import net.minecraft.server.level.ServerPlayer;

public final class LinkServerComputer extends ServerComputer {
    private final LinkBrain brain;
    private int oldLightColour = -1;
    @Nullable
    private ComputerState oldComputerState;
    private Set<ServerPlayer> tracking = Set.of();

    LinkServerComputer(LinkBrain brain, LinkHolder holder, ServerComputer.Properties properties) {
        super(holder.level(), holder.blockPos(), properties
                .terminalSize(Config.TURTLE_TERM_WIDTH,Config.TURTLE_TERM_HEIGHT)
                .addComponent(CCLinkComponents.LINK, brain)
                .addComponent(ComputerComponents.POCKET, brain)
        );

        this.brain = brain;
    }

    public LinkBrain getBrain() {
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

