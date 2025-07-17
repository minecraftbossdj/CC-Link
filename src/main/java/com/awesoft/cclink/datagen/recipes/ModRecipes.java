package com.awesoft.cclink.datagen.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "cclink");

    public static final RegistryObject<RecipeSerializer<IntegratedLinkComputerCopyRecipe>> INTEGRATED_LINK_COMPUTER_COPY =
            SERIALIZERS.register("integrated_link_computer_copy", IntegratedLinkComputerCopyRecipe.Serializer::new); //ik this is ass, im sorry, but this is my first time messing with datagen

    public static final RegistryObject<RecipeSerializer<LinkTurtleIdCopyRecipe>> LINK_TURTLE_ID_COPY =
            SERIALIZERS.register("link_turtle_id_copy", LinkTurtleIdCopyRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<LinkUpgradeRecipeType>> LINK_UPGRADE =
            SERIALIZERS.register("link_upgrade", LinkUpgradeRecipeType.Serializer::new); // see i did better here :speak: :fire:

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}