package com.awesoft.cclink.item.EntityLink;


import com.awesoft.cclink.Registration.CCLinkComponents;
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

public final class EntityLinkServerComputer extends ServerComputer {
    private final EntityLinkBrain brain;
    private int oldLightColour = -1;
    @Nullable
    private ComputerState oldComputerState;
    private Set<ServerPlayer> tracking = Set.of();

    EntityLinkServerComputer(EntityLinkBrain brain, EntityLinkHolder holder, Properties properties) {
        super(holder.level(), holder.blockPos(), properties
                .terminalSize((Integer)ConfigSpec.pocketTermWidth.get(), (Integer)ConfigSpec.computerTermHeight.get()-6) //i like being special okay, ik im a freak, i could easily just put 13, but nah id rather subtract 6
                .addComponent(CCLinkComponents.ENTITYLINK, brain)
                .addComponent(ComputerComponents.POCKET, brain)
        );

        this.brain = brain;
    }

    public EntityLinkBrain getBrain() {
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

