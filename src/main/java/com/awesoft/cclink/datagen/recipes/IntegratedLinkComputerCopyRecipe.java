package com.awesoft.cclink.datagen.recipes;

import com.awesoft.cclink.Registration.ItemRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

public class IntegratedLinkComputerCopyRecipe implements CraftingRecipe {
    private final ShapedRecipe original;

    public IntegratedLinkComputerCopyRecipe(ShapedRecipe original) {
        this.original = original;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return original.matches(container, level);
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack output = original.assemble(craftingContainer,registryAccess).copy();

        for (int i = 0; i < craftingContainer.getContainerSize(); i++) {
            ItemStack stack = craftingContainer.getItem(i);
            if (stack.getItem() == ItemRegistry.LINK_CORE.get()) {
                if (stack.hasTag() && stack.getTag().contains("ComputerId")) {
                    output.getOrCreateTag().put("ComputerId", stack.getTag().get("ComputerId"));
                    if (stack.getTag().contains("Label")) {
                        output.getTag().put("Label", stack.getTag().get("Label"));
                    }
                    break;
                }
            }
        }

        return output;
    }



    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return original.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(ItemRegistry.INTEGRATED_LINK_CORE.get());
    }


    @Override
    public ResourceLocation getId() {
        return original.getId();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.INTEGRATED_LINK_COMPUTER_COPY.get();
    }


    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    public static class Serializer implements RecipeSerializer<IntegratedLinkComputerCopyRecipe> {
        @Override
        public IntegratedLinkComputerCopyRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe shaped = ShapedRecipe.Serializer.SHAPED_RECIPE.fromJson(id, json);
            return new IntegratedLinkComputerCopyRecipe(shaped);
        }

        @Override
        public IntegratedLinkComputerCopyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            ShapedRecipe shaped = ShapedRecipe.Serializer.SHAPED_RECIPE.fromNetwork(id, buffer);
            return new IntegratedLinkComputerCopyRecipe(shaped);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, IntegratedLinkComputerCopyRecipe recipe) {
            ShapedRecipe.Serializer.SHAPED_RECIPE.toNetwork(buffer, recipe.original);
        }
    }
}
