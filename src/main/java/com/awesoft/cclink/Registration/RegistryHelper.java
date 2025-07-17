package com.awesoft.cclink.Registration;

import com.awesoft.cclink.CCLink;
import dan200.computercraft.shared.platform.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RegistryHelper<R, T extends R> {
    private final DeferredRegister<R> deferred;

    public RegistryHelper(ResourceKey<? extends Registry<R>> registryKey) {
        this.deferred = DeferredRegister.create(registryKey, CCLink.MODID);
    }

    /** Call once in your mod constructor: */
    public void registerBus(IEventBus bus) {
        deferred.register(bus);
    }

    /**
     * Register a T (which extends R) under cclink modid,
     * and get back a CC:T RegistryEntry<T> that you can hand
     * straight into any Turtle constructor.
     */
    public RegistryEntry<T> register(String name, Supplier<? extends T> sup) {
        RegistryObject<R> obj = deferred.register(name, sup);
        //noinspection unchecked
        return new RegistryEntry<>() {
            @Override public ResourceLocation id()    { return obj.getId(); }
            @Override public T            get()       { return (T) obj.get(); }
        };
    }
}