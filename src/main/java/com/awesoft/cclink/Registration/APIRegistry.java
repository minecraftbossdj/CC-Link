package com.awesoft.cclink.Registration;

import com.awesoft.cclink.block.LinkTurtle.TurtleLinkAPI;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkAPI;
import com.awesoft.cclink.item.LinkCore.LinkAPI;
import dan200.computercraft.api.ComputerCraftAPI;

public class APIRegistry {
    public static void register() {
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocketAccess = computer.getComponent(CCLinkComponents.LINK);
            return pocketAccess == null ? null : new LinkAPI(computer, pocketAccess);
        });
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocketAccess = computer.getComponent(CCLinkComponents.INTEGRATED_LINK);
            return pocketAccess == null ? null : new IntegratedLinkAPI(computer, pocketAccess);
        });
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var turtleAccess = computer.getComponent(CCLinkComponents.TURTLE_LINK);
            return turtleAccess == null ? null : new TurtleLinkAPI(computer, turtleAccess);
        });
    }
}
