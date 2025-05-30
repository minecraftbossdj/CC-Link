package com.awesoft.cclink;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CCLinkConfig {
    public static ForgeConfigSpec.IntValue HUD_PACKET_COOLDOWN_TICKS;

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("hud");

        HUD_PACKET_COOLDOWN_TICKS = builder
                .comment("Minimum ticks between HUD packet updates per player (0 = no cooldown)")
                .defineInRange("packetCooldownTicks", 1, 0, 100);

        builder.pop();

        SPEC = builder.build();
    }

    public static void register(ModLoadingContext context){
        context.registerConfig(ModConfig.Type.SERVER, SPEC, CCLink.MODID+"-config.toml");
    }
}
