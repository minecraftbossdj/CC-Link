package com.awesoft.cclink.Registration;

import com.awesoft.cclink.gui.LinkMenu;
import com.awesoft.cclink.gui.integrated.IntegratedLinkMenu;
import com.awesoft.cclink.gui.integrated.IntegratedLinkScreen;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.network.container.ContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.platform.RegistrationHelper;
import dan200.computercraft.shared.platform.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class CCMenus {

    private static final RegistrationHelper<MenuType<?>> REGISTRY = PlatformHelper.get().createRegistrationHelper(Registries.MENU);

    public static final RegistryEntry<MenuType<LinkMenu>> LINK_MENU = REGISTRY.register("link",
            () -> ContainerData.toType(ComputerContainerData::new, LinkMenu::ofMenuData));

    public static final RegistryEntry<MenuType<IntegratedLinkMenu>> INTEGRATED_LINK_MENU = REGISTRY.register("integrated_link",
            () -> ContainerData.toType(ComputerContainerData::new, IntegratedLinkMenu::ofMenuData));

    public static void registerMenu() {
        REGISTRY.register();
    }

}
