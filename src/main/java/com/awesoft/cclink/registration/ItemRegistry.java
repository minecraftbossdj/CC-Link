package com.awesoft.cclink.registration;


import com.awesoft.cclink.CCLinkArmorMaterials;
import com.awesoft.cclink.item.*;
import com.awesoft.cclink.item.linkCore.integrated.IntegratedLinkCoreComputerItem;
import com.awesoft.cclink.item.linkCore.LinkCoreComputerItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.turtle.items.TurtleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
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


    public static final RegistryObject<Item> MODULAR_LINK_HELMET = ITEMS.register("link_helmet",
            () -> new ModularLinkArmor(new Item.Properties().stacksTo(1), ArmorItem.Type.HELMET, CCLinkArmorMaterials.LINK_ARMOR));

    public static final RegistryObject<Item> MODULAR_LINK_CHESTPLATE = ITEMS.register("link_chestplate",
            () -> new ModularLinkArmor(new Item.Properties().stacksTo(1), ArmorItem.Type.CHESTPLATE, CCLinkArmorMaterials.LINK_ARMOR));

    public static final RegistryObject<Item> MODULAR_LINK_LEGGINGS = ITEMS.register("link_leggings",
            () -> new ModularLinkArmor(new Item.Properties().stacksTo(1), ArmorItem.Type.LEGGINGS, CCLinkArmorMaterials.LINK_ARMOR));

    public static final RegistryObject<Item> MODULAR_LINK_BOOTS = ITEMS.register("link_boots",
            () -> new ModularLinkArmor(new Item.Properties().stacksTo(1), ArmorItem.Type.BOOTS, CCLinkArmorMaterials.LINK_ARMOR));

    public static final RegistryObject<Item> CONFIGURATOR_IRON = ITEMS.register("configurator_iron",
            () -> new ArmorConfiguratorItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CONFIGURATOR_GOLD = ITEMS.register("configurator_gold",
            () -> new ArmorConfiguratorItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CONFIGURATOR_DIAMOND = ITEMS.register("configurator_diamond",
            () -> new ArmorConfiguratorItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<BlockItem> LINK_TURTLE_ADVANCED = ITEMS.register("link_turtle_advanced",()-> new TurtleItem(BlockRegistry.LINK_TURTLE_ADVANCED.get(),new Item.Properties()));


    public static final RegistryObject<Item> LINK_KEY = ITEMS.register("link_key",()-> new LinkKeyItem(new Item.Properties()));

    //upgrades
    public static final RegistryObject<Item> SCANNER_UPGRADE = ITEMS.register("scanner_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> SENSOR_UPGRADE = ITEMS.register("sensor_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> INTROSPECTION_UPGRADE = ITEMS.register("introspection_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> WORLD_UPGRADE = ITEMS.register("world_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> OVERLAY_UPGRADE = ITEMS.register("overlay_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> KINETIC_UPGRADE = ITEMS.register("kinetic_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> CHATTY_UPGRADE = ITEMS.register("chatty_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> LASER_UPGRADE = ITEMS.register("laser_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),1));


    //armor upgrades
    public static final RegistryObject<Item> BATTERY_UPGRADE_1 = ITEMS.register("battery_upgrade_tier1",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));
    public static final RegistryObject<Item> BATTERY_UPGRADE_2 = ITEMS.register("battery_upgrade_tier2",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));
    public static final RegistryObject<Item> BATTERY_UPGRADE_3 = ITEMS.register("battery_upgrade_tier3",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> SHIELD_UPGRADE_1 = ITEMS.register("shield_upgrade_tier1",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));
    public static final RegistryObject<Item> SHIELD_UPGRADE_2 = ITEMS.register("shield_upgrade_tier2",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));
    public static final RegistryObject<Item> SHIELD_UPGRADE_3 = ITEMS.register("shield_upgrade_tier3",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));

    public static final RegistryObject<Item> SHIELD_CONTROLLER_UPGRADE = ITEMS.register("shield_controller_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),0));
    public static final RegistryObject<Item> SHIELD_CONTROLLER_CREATIVE_UPGRADE = ITEMS.register("shield_controller_creative_upgrade",
            () -> new UpgradeItem(new Item.Properties().stacksTo(1),2)); //shhhhh

    //crafting resources

    public static final RegistryObject<Item> LINK_PLATING = ITEMS.register("link_plating",() -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> LINK_POWER_CORE = ITEMS.register("link_power_core",() -> new Item(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> BATTERY_TIER1 = ITEMS.register("battery_tier1",() -> new Item(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> BATTERY_TIER2 = ITEMS.register("battery_tier2",() -> new Item(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> BATTERY_TIER3 = ITEMS.register("battery_tier3",() -> new Item(new Item.Properties().stacksTo(4)));

    //peripherals
    public static final RegistryObject<Item> LINK_ARMOR_MANAGER = ITEMS.register("link_armor_manager",() -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }


}
