package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;

import javax.annotation.Nullable;

/**
 * Recipe serializer for blood infuser recipes
 * @author rubensworks
 */
public class RecipeSerializerBloodInfuser extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<RecipeBloodInfuser> {

    @Override
    public RecipeBloodInfuser read(ResourceLocation recipeId, JsonObject json) {
        JsonObject result = JSONUtils.getJsonObject(json, "result");

        // Input
        Ingredient inputIngredient = RecipeSerializerHelpers.getJsonIngredient(json, "item", false);
        FluidStack inputFluid = RecipeSerializerHelpers.getJsonFluidStack(json, "fluid", false);
        int inputTier = JSONUtils.getInt(json, "tier", 0);

        // Output
        ItemStack outputItemStack = RecipeSerializerHelpers.getJsonItemStackOrTag(result, false);

        // Other stuff
        int duration = JSONUtils.getInt(json, "duration");
        int xp = JSONUtils.getInt(json, "xp", 0);

        // Validation
        if (inputIngredient.hasNoMatchingItems() && inputFluid.isEmpty()) {
            throw new JsonSyntaxException("An input item or fluid is required");
        }
        if (outputItemStack.isEmpty()) {
            throw new JsonSyntaxException("An output item is required");
        }
        if (inputTier < 0) {
            throw new JsonSyntaxException("Tiers can not be negative");
        }
        if (duration <= 0) {
            throw new JsonSyntaxException("Durations must be higher than one tick");
        }
        if (xp < 0) {
            throw new JsonSyntaxException("XP can not be negative");
        }

        return new RecipeBloodInfuser(recipeId, inputIngredient, inputFluid, inputTier, outputItemStack, duration, xp);
    }

    @Nullable
    @Override
    public RecipeBloodInfuser read(ResourceLocation recipeId, PacketBuffer buffer) {
        // Input
        Ingredient inputIngredient = Ingredient.read(buffer);
        FluidStack inputFluid = FluidStack.readFromPacket(buffer);
        int inputTier = buffer.readVarInt();

        // Output
        ItemStack outputItemStack = buffer.readItemStack();

        // Other stuff
        int duration = buffer.readVarInt();
        int xp = buffer.readVarInt();

        return new RecipeBloodInfuser(recipeId, inputIngredient, inputFluid, inputTier, outputItemStack, duration, xp);
    }

    @Override
    public void write(PacketBuffer buffer, RecipeBloodInfuser recipe) {
        // Input
        recipe.getInputIngredient().write(buffer);
        recipe.getInputFluid().writeToPacket(buffer);
        buffer.writeVarInt(recipe.getInputTier());

        // Output
        buffer.writeItemStack(recipe.getOutputItem());

        // Other stuff
        buffer.writeVarInt(recipe.getDuration());
        buffer.writeVarInt(recipe.getXp());
    }
}
