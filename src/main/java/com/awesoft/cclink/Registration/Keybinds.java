package com.awesoft.cclink.Registration;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyMapping OPEN_LINK = new KeyMapping(
            "key.cclink.open_link",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.cclink"
    );

}
