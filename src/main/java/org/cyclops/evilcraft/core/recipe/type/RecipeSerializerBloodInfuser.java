package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;

import javax.annotation.Nullable;

/**
 * Recipe serializer for blood infuser recipes
 * @author rubensworks
 */
public class RecipeSerializerBloodInfuser implements RecipeSerializer<RecipeBloodInfuser> {

    @Override
    public RecipeBloodInfuser fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonObject result = GsonHelper.getAsJsonObject(json, "result");

        // Input
        Ingredient inputIngredient = RecipeSerializerHelpers.getJsonIngredient(json, "item", false);
        FluidStack inputFluid = RecipeSerializerHelpers.getJsonFluidStack(json, "fluid", false);
        int inputTier = GsonHelper.getAsInt(json, "tier", 0);

        // Output
        Either<ItemStack, ItemStackFromIngredient> outputItemStack = RecipeSerializerHelpers.getJsonItemStackOrTag(result, false);

        // Other stuff
        int duration = GsonHelper.getAsInt(json, "duration");
        float xp = GsonHelper.getAsFloat(json, "xp", 0);

        // Validation
        if (inputIngredient.isEmpty() && inputFluid.isEmpty()) {
            throw new JsonSyntaxException("An input item or fluid is required");
        }
        if (outputItemStack.left().isPresent() && outputItemStack.left().isEmpty()) {
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
    public RecipeBloodInfuser fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        // Input
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        FluidStack inputFluid = FluidStack.readFromPacket(buffer);
        int inputTier = buffer.readVarInt();

        // Output
        Either<ItemStack, ItemStackFromIngredient> outputItem = RecipeSerializerHelpers.readItemStackOrItemStackIngredient(buffer);

        // Other stuff
        int duration = buffer.readVarInt();
        float xp = buffer.readFloat();

        return new RecipeBloodInfuser(recipeId, inputIngredient, inputFluid, inputTier, outputItem, duration, xp);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeBloodInfuser recipe) {
        // Input
        recipe.getInputIngredient().toNetwork(buffer);
        recipe.getInputFluid().writeToPacket(buffer);
        buffer.writeVarInt(recipe.getInputTier());

        // Output
        RecipeSerializerHelpers.writeItemStackOrItemStackIngredient(buffer, recipe.getOutputItem());

        // Other stuff
        buffer.writeVarInt(recipe.getDuration());
        buffer.writeFloat(recipe.getXp());
    }
}
