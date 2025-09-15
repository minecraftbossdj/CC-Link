package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.libs.MiscLib;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AmethystBlock;
import org.openjdk.nashorn.api.tree.ImportEntryTree;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorldUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;

    public WorldUpgradeFunctions(Entity entity) {
        this.entity = entity;
    }

    public ILuaFunction getWeather = args -> {
        if (entity.level().isRainingAt(entity.getOnPos())) {
            if (entity.level().isThundering()) {
                return MethodResult.of("thundering");
            } else {
                return MethodResult.of("raining");
            }
        } else {
            return MethodResult.of("clear");
        }

    };

    public ILuaFunction getDimension = args -> {
        return MethodResult.of(entity.level().dimension().location().toString());
    };


    public ILuaFunction getBiome = args -> {
        Map<String, Object> biomeinfo = new HashMap<>();

        Holder<Biome> FeetBiomeHolder = entity.level().getBiomeManager().getBiome(entity.getOnPos());
        Optional<ResourceKey<Biome>> FeetBiomeKey = FeetBiomeHolder.unwrapKey();
        Biome FeetBiome = FeetBiomeHolder.get();
        if (FeetBiomeKey.isPresent()) {
            biomeinfo.put("name",FeetBiomeKey.get().location().toString());
        } else {
            CCLink.LOGGER.info("HOW??? WHAT HOW IS THERE NO REGISTRY KEY FOR A BIOME??? WHAT");
        }

        biomeinfo.put("canSnow",FeetBiome.coldEnoughToSnow(entity.getOnPos()));
        biomeinfo.put("canRain",FeetBiome.warmEnoughToRain(entity.getOnPos()));
        biomeinfo.put("fogColor",FeetBiome.getFogColor());
        biomeinfo.put("foliageColor",FeetBiome.getFoliageColor());

        return MethodResult.of(biomeinfo);
    };

    public ILuaFunction canSeeSky = args -> {
        return MethodResult.of(entity.level().canSeeSky(entity.getOnPos()));
    };

    public ILuaFunction isDay = args -> {
        return MethodResult.of(entity.level().isDay());
    };

    //TODO: add more shit so this isnt utterly useless :sob:

    public Map<String, Object> getFunctions() {
        functions.put("getWeather",getWeather);
        functions.put("getDimension",getDimension);
        functions.put("getBiome",getBiome);
        functions.put("canSeeSky",canSeeSky);
        functions.put("isDay",isDay);
        return functions;
    }

}
