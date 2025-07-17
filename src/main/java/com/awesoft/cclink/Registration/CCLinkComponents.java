package com.awesoft.cclink.Registration;

import dan200.computercraft.api.component.AdminComputer;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;

public class CCLinkComponents {
    public static final ComputerComponent<IPocketAccess> LINK = ComputerComponent.create("cclink", "link");
    public static final ComputerComponent<IPocketAccess> INTEGRATED_LINK = ComputerComponent.create("cclink", "integrated_link");
    public static final ComputerComponent<ITurtleAccess> TURTLE_LINK = ComputerComponent.create("cclink", "turtle_link");

    public CCLinkComponents() {
    }
}
