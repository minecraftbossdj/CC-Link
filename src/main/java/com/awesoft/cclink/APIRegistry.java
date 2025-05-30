package com.awesoft.cclink;

import com.awesoft.cclink.item.LinkCore.LinkAPI;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.shared.computer.apis.CommandAPI;

public class APIRegistry {
    public static void register() {
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocketAccess = computer.getComponent(CCLinkComponents.LINK);
            return pocketAccess == null ? null : new LinkAPI(computer, pocketAccess);
        });
    }
}
