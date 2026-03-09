package com.awesoft.cclink.upgrades.luaFunctions;

import com.awesoft.cclink.registration.ItemRegistry;
import com.awesoft.cclink.item.ModularLinkArmor;
import com.awesoft.cclink.upgrades.luaFunctions.base.UpgradeFunctionsBase;
import dan200.computercraft.api.lua.ILuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.awesoft.cclink.CCLinkConfig.KINETIC_ENERGY_MULTIPLIER;

public class KineticUpgradeFunctions extends UpgradeFunctionsBase {
    public Map<String, Object> functions = new HashMap<>();

    boolean isLink;

    public KineticUpgradeFunctions(Entity entity, boolean isLink, boolean isIntegrated) {
        this.entity = entity;
        this.isLink = isLink;
        this.UPGRADE = ItemRegistry.KINETIC_UPGRADE.get();
        this.isIntegrated = isIntegrated;
    }

    public ILuaFunction launch = args -> {
        if (!checkUpgrade()) return MethodResult.of(false, "Upgrade not equipped!");
        ItemStack leggings = null;
        if (!isLink) {
            if (entity instanceof ServerPlayer plr) {
                if (plr.getItemBySlot(EquipmentSlot.LEGS).is(ItemRegistry.MODULAR_LINK_LEGGINGS.get()))
                    leggings = plr.getItemBySlot(EquipmentSlot.LEGS);
            }
            if (leggings == null) return null;
        }

        float power = (float) args.getDouble(2);

        AtomicBoolean canRun = new AtomicBoolean(false);
        if (!isLink) {
            if (leggings.getItem() instanceof ModularLinkArmor) {
                leggings.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                    int energyAmnt = (int) (power * KINETIC_ENERGY_MULTIPLIER.get().floatValue());
                    if (energy.getEnergyStored() >= energyAmnt) {
                        energy.extractEnergy(energyAmnt, false);
                        canRun.set(true);
                    } else {
                        canRun.set(false);
                    }
                });
            }
        }
        if (isLink) canRun.set(true);
        if (power <= 4 && canRun.get()) {
            launch(entity, (float) args.getDouble(0), (float) args.getDouble(1), (float) args.getDouble(2));
            return MethodResult.of(true);
        }
        if (power > 4) {
            return MethodResult.of(false, "Power cannot be above 4!");
        } else {
            return MethodResult.of(false, "Not Enough Energy!");
        }
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
