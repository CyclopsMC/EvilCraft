package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Recipe serializer for predefined output items.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutput implements RecipeSerializer<RecipeCraftingShapedCustomOutput> {

    private final Supplier<ItemStack> outputProvider;
    @Nullable
    private final IOutputTransformer outputTransformer;

    public RecipeSerializerCraftingShapedCustomOutput(Supplier<ItemStack> outputProvider, @Nullable IOutputTransformer outputTransformer) {
        this.outputProvider = outputProvider;
        this.outputTransformer = outputTransformer;
    }

    public RecipeSerializerCraftingShapedCustomOutput(Supplier<ItemStack> outputProvider) {
        this(outputProvider, null);
    }

    @Nullable
    public IOutputTransformer getOutputTransformer() {
        return outputTransformer;
    }

    // Partially copied from ShapedRecipe.Serializer

    @Override
    public RecipeCraftingShapedCustomOutput fromJson(ResourceLocation recipeId, JsonObject json) {
        String s = GsonHelper.getAsString(json, "group", "");
        Map<String, Ingredient> map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
        String[] astring = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = ShapedRecipe.dissolvePattern(astring, map, i, j);
        ItemStack itemstack = this.outputProvider.get(); // This line is different
        return new RecipeCraftingShapedCustomOutput(this, recipeId, s, i, j, nonnulllist, itemstack);
    }

    @Override
    public RecipeCraftingShapedCustomOutput fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int i = buffer.readVarInt();
        int j = buffer.readVarInt();
        String s = buffer.readUtf(32767);
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

        for(int k = 0; k < nonnulllist.size(); ++k) {
            nonnulllist.set(k, Ingredient.fromNetwork(buffer));
        }

        ItemStack itemstack = buffer.readItem();
        return new RecipeCraftingShapedCustomOutput(this, recipeId, s, i, j, nonnulllist, itemstack);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeCraftingShapedCustomOutput recipe) {
        buffer.writeVarInt(recipe.getRecipeWidth());
        buffer.writeVarInt(recipe.getRecipeHeight());
        buffer.writeUtf(recipe.getGroup());

        for(Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem());
    }

    public static interface IOutputTransformer {
        public ItemStack transform(CraftingContainer inventory, ItemStack staticOutput);
    }
}
