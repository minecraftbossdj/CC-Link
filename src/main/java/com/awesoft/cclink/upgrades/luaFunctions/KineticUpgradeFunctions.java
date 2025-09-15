package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.hudoverlay.packets.HUDOverlayUpdatePacket;
import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.pocket.IPocketAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class KineticUpgradeFunctions {
    public Map<String, Object> functions = new HashMap<>();

    Entity entity;

    public KineticUpgradeFunctions(Entity entity) {
        this.entity = entity;
    }

    public ILuaFunction launch = args -> {
        float power = (float) args.getDouble(2);
        if (power <= 4) {
            launch(entity, (float) args.getDouble(0), (float) args.getDouble(1), (float) args.getDouble(2));
            return MethodResult.of(true);
        }
        return MethodResult.of(false,"Power cannot be above 4!");
    };

    private static final double TERMINAL_VELOCITY = -2;

    public static void launch(Entity entity, float yaw, float pitch, float power) {
        float motionX = (float) (-Math.sin(yaw / 180.0f * (float) Math.PI) * Math.cos(pitch / 180.0f * (float) Math.PI));
        float motionZ = (float) (Math.cos(yaw / 180.0f * (float) Math.PI) * Math.cos(pitch / 180.0f * (float) Math.PI));
        float motionY = (float) -Math.sin(pitch / 180.0f * (float) Math.PI);

        power /= (float) Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        if (entity instanceof LivingEntity living && living.isFallFlying()) {
            power *= 0.4F;
        }

        Vec3 currentMotion = entity.getDeltaMovement();
        Vec3 addedMotion = new Vec3(
                motionX * power,
                motionY * power * 0.5,
                motionZ * power
        );
        entity.setDeltaMovement(currentMotion.add(addedMotion));
        entity.hurtMarked = true;

        if (motionY > 0) {
            double entityVelY = entity.getDeltaMovement().y;
            if (entityVelY > 0) {
                entity.fallDistance = 0;
            } else if (entityVelY > TERMINAL_VELOCITY) {
                entity.fallDistance *= entityVelY / TERMINAL_VELOCITY;
            }
        }

        //TODO: make it not trigger anti cheat, for now players should always turn off flyingAllowed on modded servers anyways
    }

    public Map<String, Object> getFunctions() {
        functions.put("launch",launch);
        return functions;
    }

}
