package com.awesoft.cclink.events;

import com.awesoft.cclink.item.ModularLinkArmor;
import com.awesoft.cclink.libs.MiscLib;
import com.awesoft.cclink.libs.ModularArmorLib;
import com.awesoft.cclink.registration.ItemRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HurtEvent {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (ModularArmorLib.isFullModular(player) && ModularArmorLib.hasModule(player, ItemRegistry.SHIELD_CONTROLLER_UPGRADE.get())) {
                ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
                if (chestStack.getItem() instanceof ModularLinkArmor) {
                    chestStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy ->{
                        if (energy.getEnergyStored() >= event.getAmount()*1000 && !MiscLib.isArmorPiercing(event.getSource())) {
                            energy.extractEnergy((int) (event.getAmount()*1000),false);
                            event.setCanceled(true);
                        }
                    });
                }
            } else if (ModularArmorLib.hasModule(player, ItemRegistry.SHIELD_CONTROLLER_CREATIVE_UPGRADE.get())) {
                event.setCanceled(true);
            }
        }
    }
}
