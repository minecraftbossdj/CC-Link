package com.awesoft.cclink.datagen;

import com.awesoft.cclink.Registration.BlockRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.data.BlockNamedEntityLootCondition;
import dan200.computercraft.shared.data.HasComputerIdLootCondition;
import dan200.computercraft.shared.data.PlayerCreativeLootCondition;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BlockLoot {
    public static void register(BiConsumer<ResourceLocation, LootTable.Builder> add) {
        computerDrop(add, BlockRegistry.LINK_TURTLE_ADVANCED::get);
        computerDrop(add, BlockRegistry.SECURE_COMPUTER_ADVANCED::get);
    }

    private static void computerDrop(
            BiConsumer<ResourceLocation, LootTable.Builder> add,
            Supplier<? extends Block> block
    ) {
        ResourceLocation tableId = block.get().getLootTable();
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(1))
                .add(DynamicLoot.dynamicEntry(new ResourceLocation(ComputerCraftAPI.MOD_ID, "computer")))
                .when(
                        AnyOfCondition.anyOf(
                                BlockNamedEntityLootCondition.BUILDER,
                                HasComputerIdLootCondition.BUILDER,
                                PlayerCreativeLootCondition.BUILDER.invert()
                        )
                )
                ;
        add.accept(tableId, LootTable.lootTable().withPool(pool));
    }
}
