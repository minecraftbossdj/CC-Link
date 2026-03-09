package com.awesoft.cclink.registration;

import com.awesoft.cclink.gui.LinkMenu;
import com.awesoft.cclink.gui.armor.ArmorMenu;
import com.awesoft.cclink.gui.integrated.IntegratedLinkMenu;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.network.container.ContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.platform.RegistrationHelper;
import dan200.computercraft.shared.platform.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, "cclink");

    private static final RegistrationHelper<MenuType<?>> REGISTRY = PlatformHelper.get().createRegistrationHelper(Registries.MENU);

    public static final RegistryEntry<MenuType<LinkMenu>> LINK_MENU = REGISTRY.register("link",
            () -> ContainerData.toType(ComputerContainerData::new, LinkMenu::ofMenuData));

    public static final RegistryEntry<MenuType<IntegratedLinkMenu>> INTEGRATED_LINK_MENU = REGISTRY.register("integrated_link",
            () -> ContainerData.toType(ComputerContainerData::new, IntegratedLinkMenu::ofMenuData));

    public static final RegistryObject<MenuType<ArmorMenu>> ARMOR_MENU = MENUS.register(
            "modular_armor",
            () -> IForgeMenuType.create((windowId, inv, buf) -> new ArmorMenu(windowId, inv, buf))
    );


    public static void registerMenu(IEventBus bus) {
        REGISTRY.register();
        MENUS.register(bus);
    }

}
