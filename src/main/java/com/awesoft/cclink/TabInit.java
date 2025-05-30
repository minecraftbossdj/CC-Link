package com.awesoft.cclink;

import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.platform.RegistrationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Set;

@Mod.EventBusSubscriber(modid = CCLink.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCLink.MODID);

    public static final RegistryObject<CreativeModeTab> CCLINK_TAB = TabInit.CREATIVE_MODE_TABS.register("cclink.main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cclink.main"))
                    .icon(() -> new ItemStack(ItemRegistry.LINK_CORE.get()))
                    .displayItems((displayParms,output) ->{
                        output.accept(ItemRegistry.LINK_CORE.get());
                        output.accept(ItemRegistry.LINK_INTERFACE.get());
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
