package com.awesoft.cclink.datagen;

import com.awesoft.cclink.CCLink;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CCLink.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        var helper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new TagProvider(output, lookupProvider, helper));
        generator.addProvider(
                event.includeServer(),
                ModLootTableProvider.create(output)
        );
    }
}
