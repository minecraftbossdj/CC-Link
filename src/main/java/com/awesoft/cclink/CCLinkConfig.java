package com.awesoft.cclink;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CCLinkConfig {
    public static ForgeConfigSpec.IntValue HUD_PACKET_COOLDOWN_TICKS;
    public static ForgeConfigSpec.IntValue TIER1_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue TIER2_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue TIER3_MAX_ENERGY;
    public static ForgeConfigSpec.IntValue KINETIC_ENERGY_MULTIPLIER;
    public static ForgeConfigSpec.BooleanValue KINETIC_LINK_ENABLED;

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("HUD");

        HUD_PACKET_COOLDOWN_TICKS = builder
                .comment("Minimum ticks between HUD packet updates per player (0 = no cooldown)")
                .defineInRange("packetCooldownTicks", 1, 0, 100);

        builder.push("Energy Upgrades");

        TIER1_MAX_ENERGY = builder
                .comment("Additional Energy Size for every Battery Upgrade Tier 1")
                .defineInRange("tier1MaxEnergy", 10000, 0, Integer.MAX_VALUE);

        TIER2_MAX_ENERGY = builder
                .comment("Additional Energy Size for every Battery Upgrade Tier 2")
                .defineInRange("tier2MaxEnergy", 50000, 0, Integer.MAX_VALUE);

        TIER3_MAX_ENERGY = builder
                .comment("Additional Energy Size for every Battery Upgrade Tier 3")
                .defineInRange("tier2MaxEnergy", 100000, 0, Integer.MAX_VALUE);

        builder.push("Modules");

        KINETIC_ENERGY_MULTIPLIER = builder
                .comment("Energy Multiplier for Kinetic, ex: launch power 2 and default config of 5000, would be 10000FE")
                .defineInRange("kineticEnergyMultiplier", 5000, 0, Integer.MAX_VALUE);

        KINETIC_LINK_ENABLED = builder
                .comment("Kinetic Module enabled in Link (uses no power in link)")
                .define("kineticLinkEnabled", true);

        builder.pop();

        SPEC = builder.build();
    }

    public static void register(ModLoadingContext context){
        context.registerConfig(ModConfig.Type.SERVER, SPEC, CCLink.MODID+"-config.toml");
    }
}
