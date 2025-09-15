package com.awesoft.cclink.Registration;


import com.awesoft.cclink.CCLinkArmorMaterials;
import com.awesoft.cclink.item.EntityLink.EntityLinkComputerItem;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkCoreComputerItem;
import com.awesoft.cclink.item.LinkCore.LinkCoreComputerItem;
import com.awesoft.cclink.item.LinkInterfaceHelmet;
import com.awesoft.cclink.item.LinkKeyItem;
import com.awesoft.cclink.item.UpgradeItem;
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

    /*public static final RegistryObject<EntityLinkComputerItem> ENTITY_LINK = ITEMS.register("entity_link",
            () -> new EntityLinkComputerItem(new Item.Properties().stacksTo(1), ComputerFamily.ADVANCED));*/ //broken and too lazy to fix ngl


    public static final RegistryObject<Item> LINK_INTERFACE = ITEMS.register("link_interface",
            () -> new LinkInterfaceHelmet(new Item.Properties().stacksTo(1), CCLinkArmorMaterials.LINK_INTERFACE));

    public static final RegistryObject<BlockItem> LINK_TURTLE_ADVANCED = ITEMS.register("link_turtle_advanced",()-> new TurtleItem(BlockRegistry.LINK_TURTLE_ADVANCED.get(),new Item.Properties()));


    public static final RegistryObject<Item> LINK_KEY = ITEMS.register("link_key",()-> new LinkKeyItem(new Item.Properties()));

    //upgrades
    public static final RegistryObject<Item> SCANNER_UPGRADE = ITEMS.register("scanner_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> SENSOR_UPGRADE = ITEMS.register("sensor_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> INTROSPECTION_UPGRADE = ITEMS.register("introspection_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> WORLD_UPGRADE = ITEMS.register("world_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> OVERLAY_UPGRADE = ITEMS.register("overlay_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> KINETIC_UPGRADE = ITEMS.register("kinetic_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> CHATTY_UPGRADE = ITEMS.register("chatty_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),false));

    public static final RegistryObject<Item> LASER_UPGRADE = ITEMS.register("laser_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),true));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }


}
