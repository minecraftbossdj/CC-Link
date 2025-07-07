package com.awesoft.cclink.Registration;

import com.awesoft.cclink.CCLink;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TurtleUpgradeRegistry {

    public static final DeferredRegister<TurtleUpgradeSerialiser<?>> TURTLE_SERIALIZER = DeferredRegister.create(TurtleUpgradeSerialiser.registryId(), CCLink.MODID);

    //public static final RegistryObject<TurtleUpgradeSerialiser<TestTurtleUpgrade>> TEST_TURTLE = TurtleUpgradeRegistry.TURTLE_SERIALIZER.register(TurtleUpgradeRegistry.ID.TEST_TURTLE.getPath(), () -> TurtleUpgradeSerialiser.simpleWithCustomItem(TestTurtleUpgrade::new));


    public static class ID {
        public static final ResourceLocation TEST_TURTLE = new ResourceLocation(CCLink.MODID, "test_turtle");

    }

    public static void register(IEventBus bus) {
        TURTLE_SERIALIZER.register(bus);
    }
}
