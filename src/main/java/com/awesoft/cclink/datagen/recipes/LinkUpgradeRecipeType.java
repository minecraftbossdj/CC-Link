package com.awesoft.cclink.datagen.recipes;

import com.awesoft.cclink.Registration.ItemRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LinkUpgradeRecipeType extends ShapelessRecipe {
    private final ResourceLocation id;
    private final String upgrade;
    private final ItemStack result;

    public LinkUpgradeRecipeType(ResourceLocation pId, String pGroup, CraftingBookCategory pCategory, ItemStack pResult, NonNullList<Ingredient> pIngredients, String upgrade) {
        super(pId, pGroup, pCategory, pResult, pIngredients);
        this.id = pId;
        this.upgrade = upgrade;
        this.result = pResult;
    }


    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack result = getResultItem(registryAccess).copy();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.hasTag() && stack.getTag().contains("ComputerId")) {
                CompoundTag tag = stack.getTag().copy();
                tag.putString("Upgrade", upgrade);
                result.setTag(tag);
                break;
            }
        }
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.LINK_UPGRADE.get();
    }


    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    public static class Serializer implements RecipeSerializer<LinkUpgradeRecipeType> {
        @Override
        public LinkUpgradeRecipeType fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapelessRecipe base = ShapelessRecipe.Serializer.SHAPELESS_RECIPE.fromJson(recipeId, json);

            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            String upgradeId = GsonHelper.getAsString(resultObj, "upgrade");

            ItemStack result = ShapedRecipe.itemStackFromJson(resultObj);

            return new LinkUpgradeRecipeType(recipeId, base.getGroup(), base.category(), result, base.getIngredients(), upgradeId);
        }

        @Override
        public LinkUpgradeRecipeType fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {

            String group = buffer.readUtf();
            CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);

            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack result = buffer.readItem();
            String upgradeId = buffer.readUtf();

            return new LinkUpgradeRecipeType(id, group, category, result, ingredients, upgradeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, LinkUpgradeRecipeType recipe) {
            //buffer.writeResourceLocation(recipe.getId());
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());

            buffer.writeVarInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeUtf(recipe.upgrade);
        }
    }
}