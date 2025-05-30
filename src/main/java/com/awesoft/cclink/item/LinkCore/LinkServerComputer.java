package com.awesoft.cclink.item.LinkCore;


import com.awesoft.cclink.CCLinkComponents;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.config.ConfigSpec;
import dan200.computercraft.shared.network.client.PocketComputerDataMessage;
import dan200.computercraft.shared.network.client.PocketComputerDeletedClientMessage;
import dan200.computercraft.shared.network.server.ServerNetworking;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public final class LinkServerComputer extends ServerComputer {
    private final LinkBrain brain;
    private int oldLightColour = -1;
    @Nullable
    private ComputerState oldComputerState;
    private Set<ServerPlayer> tracking = Set.of();

    LinkServerComputer(LinkBrain brain, LinkHolder holder, ServerComputer.Properties properties) {
        super(holder.level(), holder.blockPos(), properties
                .terminalSize((Integer)ConfigSpec.computerTermWidth.get(), (Integer)ConfigSpec.computerTermHeight.get())
                .addComponent(CCLinkComponents.LINK, brain)
                .addComponent(ComputerComponents.POCKET, brain));
        this.brain = brain;
    }

    public LinkBrain getBrain() {
        return this.brain;
    }

   /*
    protected void tickServer() {
        super.tickServer();
        List<ServerPlayer> newTracking = this.getLevel().getChunkSource().chunkMap.getPlayers(new ChunkPos(this.getPosition()), false);
        boolean trackingChanged = this.tracking.size() != newTracking.size() || !this.tracking.containsAll(newTracking);
        ComputerState state = this.getState();
        int light = this.brain.getLight();
        if (this.oldLightColour == light && this.oldComputerState == state) {
            if (trackingChanged) {
                List<ServerPlayer> added = newTracking.stream().filter((x) -> !this.tracking.contains(x)).toList();
                if (!added.isEmpty()) {
                    ServerNetworking.sendToPlayers(new PocketComputerDataMessage(this, this.brain.holder().isTerminalAlwaysVisible()), added);
                }
            }
        } else {
            this.oldComputerState = state;
            this.oldLightColour = light;
            ServerNetworking.sendToPlayers(new PocketComputerDataMessage(this, false), newTracking);
        }

        if (trackingChanged) {
            this.tracking = Set.copyOf(newTracking);
        }

    }

    protected void onTerminalChanged() {
        ServerPlayer var10000;
        label28: {
            super.onTerminalChanged();
            PocketHolder var3 = this.brain.holder();
            if (var3 instanceof PocketHolder.PlayerHolder h) {
                if (h.isValid(this)) {
                    var10000 = h.entity();
                    break label28;
                }
            }

            var10000 = null;
        }

        ServerPlayer holder = var10000;
        if (this.brain.holder().isTerminalAlwaysVisible() && !this.tracking.isEmpty()) {
            PocketComputerDataMessage packet = new PocketComputerDataMessage(this, true);
            ServerNetworking.sendToPlayers(packet, this.tracking);
            if (holder != null && !this.tracking.contains(holder)) {
                ServerNetworking.sendToPlayer(packet, holder);
            }
        } else if (holder != null) {
            ServerNetworking.sendToPlayer(new PocketComputerDataMessage(this, true), holder);
        }

    }

    */

    protected void onRemoved() {
        super.onRemoved();
        ServerNetworking.sendToAllPlayers(new PocketComputerDeletedClientMessage(this.getInstanceUUID()), this.getLevel().getServer());
    }

}

