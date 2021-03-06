package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Recipe serializer for predefined output items.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutput extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<RecipeCraftingShapedCustomOutput> {

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
    public RecipeCraftingShapedCustomOutput read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        Map<String, Ingredient> map = ShapedRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
        String[] astring = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = ShapedRecipe.deserializeIngredients(astring, map, i, j);
        ItemStack itemstack = this.outputProvider.get(); // This line is different
        return new RecipeCraftingShapedCustomOutput(this, recipeId, s, i, j, nonnulllist, itemstack);
    }

    @Override
    public RecipeCraftingShapedCustomOutput read(ResourceLocation recipeId, PacketBuffer buffer) {
        int i = buffer.readVarInt();
        int j = buffer.readVarInt();
        String s = buffer.readString(32767);
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

        for(int k = 0; k < nonnulllist.size(); ++k) {
            nonnulllist.set(k, Ingredient.read(buffer));
        }

        ItemStack itemstack = buffer.readItemStack();
        return new RecipeCraftingShapedCustomOutput(this, recipeId, s, i, j, nonnulllist, itemstack);
    }

    @Override
    public void write(PacketBuffer buffer, RecipeCraftingShapedCustomOutput recipe) {
        buffer.writeVarInt(recipe.getRecipeWidth());
        buffer.writeVarInt(recipe.getRecipeHeight());
        buffer.writeString(recipe.getGroup());

        for(Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buffer);
        }

        buffer.writeItemStack(recipe.getRecipeOutput());
    }

    public static interface IOutputTransformer {
        public ItemStack transform(CraftingInventory inventory, ItemStack staticOutput);
    }
}
