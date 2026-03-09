package com.awesoft.cclink.registration;

import com.awesoft.cclink.CCLink;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CCLink.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCLink.MODID);

    public static final RegistryObject<CreativeModeTab> CCLINK_TAB = TabInit.CREATIVE_MODE_TABS.register("cclink.main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cclink.main"))
                    .icon(() -> new ItemStack(ItemRegistry.LINK_CORE.get()))
                    .displayItems((displayParms,output) ->{
                        output.accept(ItemRegistry.LINK_CORE.get());
                        output.accept(ItemRegistry.INTEGRATED_LINK_CORE.get());
                        output.accept(ItemRegistry.LINK_CORE_COMMAND.get());
                        //output.accept(ItemRegistry.ENTITY_LINK.get()); //broke atm
                        output.accept(ItemRegistry.LINK_INTERFACE.get());
                        //output.accept(ItemRegistry.LINK_TURTLE_ADVANCED.get()); //TODO: remove
                        //output.accept(ItemRegistry.LINK_KEY.get()); //TODO: find use
                        /*output.accept(ItemRegistry.MODULAR_LINK_HELMET.get()); //TODO: Finish armor
                        output.accept(ItemRegistry.MODULAR_LINK_CHESTPLATE.get());
                        output.accept(ItemRegistry.MODULAR_LINK_LEGGINGS.get());
                        output.accept(ItemRegistry.MODULAR_LINK_BOOTS.get());
                        output.accept(ItemRegistry.LINK_ARMOR_MANAGER.get());
                        output.accept(ItemRegistry.LINK_PLATING.get());
                        output.accept(ItemRegistry.LINK_POWER_CORE.get());
                        output.accept(ItemRegistry.BATTERY_TIER1.get());
                        output.accept(ItemRegistry.BATTERY_TIER2.get());
                        output.accept(ItemRegistry.BATTERY_TIER3.get());*/
                    })
                    .build()
    );

    public static final RegistryObject<CreativeModeTab> CCLINK_TAB_UPGRADES = TabInit.CREATIVE_MODE_TABS.register("cclink.upgrades",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cclink.upgrades"))
                    .icon(() -> new ItemStack(ItemRegistry.LINK_POWER_CORE.get()))
                    .displayItems((displayParms,output) ->{
//                        output.accept(ItemRegistry.CONFIGURATOR_IRON.get());
//                        output.accept(ItemRegistry.CONFIGURATOR_GOLD.get());
//                        output.accept(ItemRegistry.CONFIGURATOR_DIAMOND.get());
                        output.accept(ItemRegistry.SCANNER_UPGRADE.get());
                        output.accept(ItemRegistry.SENSOR_UPGRADE.get());
                        output.accept(ItemRegistry.INTROSPECTION_UPGRADE.get());
                        output.accept(ItemRegistry.WORLD_UPGRADE.get());
                        output.accept(ItemRegistry.OVERLAY_UPGRADE.get());
                        output.accept(ItemRegistry.KINETIC_UPGRADE.get());
                        output.accept(ItemRegistry.CHATTY_UPGRADE.get());
                        //output.accept(ItemRegistry.LASER_UPGRADE.get()); //WIP, comes out next update
                        /*output.accept(ItemRegistry.BATTERY_UPGRADE_1.get());
                        output.accept(ItemRegistry.BATTERY_UPGRADE_2.get());
                        output.accept(ItemRegistry.BATTERY_UPGRADE_3.get());
                        output.accept(ItemRegistry.SHIELD_UPGRADE_1.get());
                        output.accept(ItemRegistry.SHIELD_UPGRADE_2.get());
                        output.accept(ItemRegistry.SHIELD_UPGRADE_3.get());
                        output.accept(ItemRegistry.SHIELD_CONTROLLER_UPGRADE.get());*/
                    })
                    .build()
    );



    public static ItemStack makeCommandPocket(Item turtle, String upgrade) {
        ItemStack stack = new ItemStack(turtle);
        stack.getOrCreateTag().putString("Upgrade", upgrade);
        stack.getOrCreateTag().putInt("Color",12876878);
        return stack;
    }

}
