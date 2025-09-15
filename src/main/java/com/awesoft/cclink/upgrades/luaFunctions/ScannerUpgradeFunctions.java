package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.CCLink;
import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.pocket.IPocketAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ScannerUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;
    IComputerSystem computer;

    public ScannerUpgradeFunctions(Entity entity, IComputerSystem computer) {
        this.entity = entity;
        this.computer = computer;
    }


    //i can neither confirm or deny if i stole these from finerperipherals or not :clueless:
    public static void relativeTraverseBlocks(Level world, BlockPos center, int radius, BiConsumer<BlockState, BlockPos> consumer) {
        traverseBlocks(world, center, radius, consumer, true);
    }

    public static void traverseBlocks(Level world, BlockPos center, int radius, BiConsumer<BlockState, BlockPos> consumer, boolean relativePosition) {
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();
        for (int oX = x - radius; oX <= x + radius; oX++) {
            for (int oY = y - radius; oY <= y + radius; oY++) {
                for (int oZ = z - radius; oZ <= z + radius; oZ++) {
                    BlockPos subPos = new BlockPos(oX, oY, oZ);
                    BlockState blockState = world.getBlockState(subPos);
                    if (!blockState.isAir()) {
                        if (relativePosition) {
                            consumer.accept(blockState, new BlockPos(oX - x, oY - y, oZ - z));
                        } else {
                            consumer.accept(blockState, subPos);
                        }
                    }
                }
            }
        }
    }

    public static String convertToBlockId(Block blk) {
        return BuiltInRegistries.BLOCK.getKey(blk).toString();
    }

    public static <T> List<String> tagsToList(@NotNull Supplier<Stream<TagKey<T>>> tags) {
        if (tags.get().findAny().isEmpty()) return Collections.emptyList();
        return tags.get().map(ScannerUpgradeFunctions::tagToString).toList();
    }

    public static <T> String tagToString(@NotNull TagKey<T> tag) {
        return tag.registry().location() + "/" + tag.location();
    }

    private static List<Map<String, ?>> scan(Level level, BlockPos center, int radius) {
        List<Map<String, ?>> result = new ArrayList<>();
        relativeTraverseBlocks(level, center, radius, (state, pos) -> {
            HashMap<String, Object> data = new HashMap<>(6);
            data.put("x", pos.getX());
            data.put("y", pos.getY());
            data.put("z", pos.getZ());



            Block block = state.getBlock();
            data.put("name", convertToBlockId(block));
            data.put("tags", tagsToList(() -> block.builtInRegistryHolder().tags()));
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) data.put("direction", state.getValue(BlockStateProperties.HORIZONTAL_FACING).getName());

            if (state.hasProperty(BlockStateProperties.VERTICAL_DIRECTION)) data.put("direction_vertical", state.getValue(BlockStateProperties.VERTICAL_DIRECTION).getName());

            if (state.hasProperty(BlockStateProperties.HALF)) data.put("half", state.getValue(BlockStateProperties.HALF).toString());

            if (state.hasProperty(BlockStateProperties.AXIS)) data.put("axis", state.getValue(BlockStateProperties.AXIS).toString());

            if (state.hasProperty(BlockStateProperties.POWERED)) data.put("powered", state.getValue(BlockStateProperties.POWERED));

            if (state.hasProperty(BlockStateProperties.POWER)) data.put("powerlvl", state.getValue(BlockStateProperties.POWER));


            result.add(data);
        });

        return result;
    }

    private static final Map<Integer, Integer> lastCalledMap = new HashMap<>();
    private static final int cooldownTime = 2000;

    private int getRemainingCooldownTime(int computerId) {
        int lastCalled = lastCalledMap.getOrDefault(computerId, 0);
        int currentTime = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        int elapsed = currentTime - lastCalled;

        if (elapsed < 0) {
            elapsed += Integer.MAX_VALUE;
        }

        return Math.max(0, cooldownTime - elapsed);
    }

    private boolean canCallFunction(int computerId) {
        int remainingTime = getRemainingCooldownTime(computerId);
        return remainingTime <= 0;
    }

    public ILuaFunction scan = args -> {
        if (!canCallFunction(computer.getID())) {
            return MethodResult.of(null,"On cooldown!");

        }
        lastCalledMap.put(computer.getID(), (int) (System.currentTimeMillis() % Integer.MAX_VALUE)); //ok yes it MIGHT be universal but you're not NORMALLY gonna get 2 of the same ids so... who cares :clueless:

        if (args.getInt(0) > 8) return MethodResult.of(false,"Cannot be bigger than 8 block radius!"); //holy lazy
        int radius = args.getInt(0);
        return MethodResult.of(scan(entity.level(), entity.getOnPos(), radius));

    };

    public ILuaFunction getOperationCooldown = args -> MethodResult.of(getRemainingCooldownTime(computer.getID()));




    public Map<String, Object> getFunctions() {
        functions.put("scan",scan);
        functions.put("getOperationCooldown",getOperationCooldown);
        return functions;
    }

}
