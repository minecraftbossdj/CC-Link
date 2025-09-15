package com.awesoft.cclink.libs;

import net.minecraft.core.Direction;

public class MiscLib {
    
    public static Direction toDirection(String dir) {
        if (dir.equalsIgnoreCase("north")) return Direction.NORTH;
        if (dir.equalsIgnoreCase("south")) return Direction.SOUTH;
        if (dir.equalsIgnoreCase("east")) return Direction.EAST;
        if (dir.equalsIgnoreCase("west")) return Direction.WEST;
        if (dir.equalsIgnoreCase("up")) return Direction.UP;
        if (dir.equalsIgnoreCase("down")) return Direction.DOWN;
        
        return null;
    }
}
