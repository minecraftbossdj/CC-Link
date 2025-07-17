package com.awesoft.cclink.Registration;


import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.CCLinkArmorMaterials;
import com.awesoft.cclink.block.LinkTurtle.LinkTurtleBlock;
import com.awesoft.cclink.block.LinkTurtle.LinkTurtleBlockEntity;
import com.awesoft.cclink.item.LinkCore.Integrated.IntegratedLinkCoreComputerItem;
import com.awesoft.cclink.item.LinkCore.LinkCoreComputerItem;
import com.awesoft.cclink.item.LinkInterfaceHelmet;
import com.awesoft.cclink.item.LinkKeyItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.platform.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;


public class BlockRegistry {

    public static final RegistryHelper<Block, LinkTurtleBlock> LINK_TURTLE_BLOCK_REGISTER =
            new RegistryHelper<>(ForgeRegistries.BLOCKS.getRegistryKey());

    public static final RegistryHelper<BlockEntityType<?>, BlockEntityType<LinkTurtleBlockEntity>> LINK_TURTLE_BLOCK_ENTITY_REGISTER =
            new RegistryHelper<>(ForgeRegistries.BLOCK_ENTITY_TYPES.getRegistryKey());


    public static final RegistryEntry<BlockEntityType<LinkTurtleBlockEntity>> LINK_TURTLE_ADVANCED_ENTITY =
            LINK_TURTLE_BLOCK_ENTITY_REGISTER.register("link_turtle_advanced", () ->
                    BlockEntityType.Builder.<LinkTurtleBlockEntity>of(
                            (pos, state) -> new LinkTurtleBlockEntity(
                                    BlockRegistry.LINK_TURTLE_ADVANCED_ENTITY.get(),
                                    pos, state,
                                    () -> Config.advancedTurtleFuelLimit,
                                    ComputerFamily.ADVANCED
                            ),
                            BlockRegistry.LINK_TURTLE_ADVANCED.get()
                    ).build(null)
            );

    public static final RegistryEntry<LinkTurtleBlock> LINK_TURTLE_ADVANCED =
            LINK_TURTLE_BLOCK_REGISTER.register("link_turtle_advanced", () ->
                    new LinkTurtleBlock(
                            BlockBehaviour.Properties.of().strength(2.5f),
                            LINK_TURTLE_ADVANCED_ENTITY
                    )
            );

    public static void register(IEventBus bus) {
        LINK_TURTLE_BLOCK_REGISTER.registerBus(bus);
        LINK_TURTLE_BLOCK_ENTITY_REGISTER.registerBus(bus);
    }


}
