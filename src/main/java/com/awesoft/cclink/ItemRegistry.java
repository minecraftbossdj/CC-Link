package com.awesoft.cclink;


import com.awesoft.cclink.item.LinkCore.LinkCoreComputerItem;
import com.awesoft.cclink.item.LinkInterfaceHelmet;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.data.models.blockstates.PropertyDispatch.properties;


public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "cclink");

    public static final RegistryObject<LinkCoreComputerItem> LINK_CORE = ITEMS.register("link_core",
            () -> new LinkCoreComputerItem(new Item.Properties().stacksTo(1), ComputerFamily.ADVANCED));

    public static final RegistryObject<LinkCoreComputerItem> LINK_CORE_COMMAND = ITEMS.register("link_core_command",
            () -> new LinkCoreComputerItem(new Item.Properties().stacksTo(1), ComputerFamily.COMMAND));


    public static final RegistryObject<Item> LINK_INTERFACE = ITEMS.register("link_interface",
            () -> new LinkInterfaceHelmet(new Item.Properties().stacksTo(1), CCLinkArmorMaterials.LINK_INTERFACE));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }


}
