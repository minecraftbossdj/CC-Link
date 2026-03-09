package com.awesoft.cclink.registration;

import com.awesoft.cclink.CCLink;
import com.awesoft.cclink.peripherals.linkArmorManager.LinkArmorManagerUpgrade;
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PocketRegistry {
    public static final DeferredRegister<PocketUpgradeSerialiser<?>> POCKET_SERIALIZER = DeferredRegister.create(PocketUpgradeSerialiser.registryId(), CCLink.MODID);

    public static final RegistryObject<PocketUpgradeSerialiser<LinkArmorManagerUpgrade>> LINK_ARMOR_POCKET = PocketRegistry.POCKET_SERIALIZER.register(ID.LINK_ARMOR_ID.getPath(), () -> PocketUpgradeSerialiser.simpleWithCustomItem(LinkArmorManagerUpgrade::new));


    public static class ID {
        public static final ResourceLocation LINK_ARMOR_ID = new ResourceLocation(CCLink.MODID, "link_armor_pocket");

    }
}
