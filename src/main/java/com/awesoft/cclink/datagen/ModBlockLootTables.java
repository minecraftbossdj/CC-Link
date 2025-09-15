package com.awesoft.cclink.datagen;

import com.awesoft.cclink.Registration.BlockRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(BlockRegistry.LINK_TURTLE_ADVANCED.get(), this::createComputerDrop);
        //this.add(BlockRegistry.SECURE_COMPUTER_ADVANCED.get(), this::createComputerDrop);
    }

    private LootTable.Builder createComputerDrop(Block block) {
        return LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(
                        LootItem.lootTableItem(block)
                            .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("ComputerId", "ComputerId")
                                .copy("Label", "Label")
                                .copy("LeftUpgrade","LeftUpgrade")
                                .copy("RightUpgrade","RightUpgrade")
                                .copy("LeftUpgradeNbt","LeftUpgradeNbt")
                                .copy("RightUpgradeNbt","RightUpgradeNbt")
                            )
                            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                    )
            );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(
                BlockRegistry.LINK_TURTLE_ADVANCED.get()/*,
                BlockRegistry.SECURE_COMPUTER_ADVANCED.get()*/
        );
    }
}


