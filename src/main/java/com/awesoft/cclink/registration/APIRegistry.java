package com.awesoft.cclink.registration;

import com.awesoft.cclink.block.LinkTurtle.TurtleLinkAPI;
import com.awesoft.cclink.item.entityLink.EntityLinkAPI;
import com.awesoft.cclink.item.linkCore.integrated.IntegratedLinkAPI;
import com.awesoft.cclink.item.linkCore.LinkAPI;
import dan200.computercraft.api.ComputerCraftAPI;

public class APIRegistry {
    public static void register() {
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocketAccess = computer.getComponent(CCLinkComponents.LINK);
            return pocketAccess == null ? null : new LinkAPI(computer, pocketAccess);
        });
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocketAccess = computer.getComponent(CCLinkComponents.INTEGRATED_LINK);
            return pocketAccess == null ? null : new IntegratedLinkAPI(computer, pocketAccess); //TODO: add if (!isPlayerAlive()) return null; to every lua function
        });
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var turtleAccess = computer.getComponent(CCLinkComponents.TURTLE_LINK);
            return turtleAccess == null ? null : new TurtleLinkAPI(computer, turtleAccess); //TODO: remove link turtle, add all the upgrades as turtle and pocket upgrades.
        });
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocketAccess = computer.getComponent(CCLinkComponents.ENTITYLINK);
            return pocketAccess == null ? null : new EntityLinkAPI(computer, pocketAccess); //TODO: FIX THIS MF
        });
    }
}
