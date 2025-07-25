package com.awesoft.cclink.Registration;


import com.awesoft.cclink.CCLinkArmorMaterials;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkCoreComputerItem;
import com.awesoft.cclink.item.LinkCore.LinkCoreComputerItem;
import com.awesoft.cclink.item.LinkInterfaceHelmet;
import com.awesoft.cclink.item.LinkKeyItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.ComputerItem;
import dan200.computercraft.shared.turtle.items.TurtleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.data.models.blockstates.PropertyDispatch.properties;


public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "cclink");

    public static final RegistryObject<LinkCoreComputerItem> LINK_CORE = ITEMS.register("link_core",
            () -> new LinkCoreComputerItem(new Item.Properties().stacksTo(1), ComputerFamily.ADVANCED));

    public static final RegistryObject<IntegratedLinkCoreComputerItem> INTEGRATED_LINK_CORE = ITEMS.register("integrated_link_core",
            () -> new IntegratedLinkCoreComputerItem(new Item.Properties().stacksTo(1), ComputerFamily.ADVANCED));

    public static final RegistryObject<LinkCoreComputerItem> LINK_CORE_COMMAND = ITEMS.register("link_core_command",
            () -> new LinkCoreComputerItem(new Item.Properties().stacksTo(1), ComputerFamily.COMMAND));


    public static final RegistryObject<Item> LINK_INTERFACE = ITEMS.register("link_interface",
            () -> new LinkInterfaceHelmet(new Item.Properties().stacksTo(1), CCLinkArmorMaterials.LINK_INTERFACE));

    public static final RegistryObject<BlockItem> LINK_TURTLE_ADVANCED = ITEMS.register("link_turtle_advanced",()-> new TurtleItem(BlockRegistry.LINK_TURTLE_ADVANCED.get(),new Item.Properties()));

    public static final RegistryObject<BlockItem> SECURE_COMPUTER_ADVANCED = ITEMS.register("secure_computer_advanced",()-> new ComputerItem(BlockRegistry.SECURE_COMPUTER_ADVANCED.get(),new Item.Properties()));

    public static final RegistryObject<Item> LINK_KEY = ITEMS.register("link_key",()-> new LinkKeyItem(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }


}
