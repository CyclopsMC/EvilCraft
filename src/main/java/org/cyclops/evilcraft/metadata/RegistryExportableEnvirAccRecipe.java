package org.cyclops.evilcraft.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.metadata.IRegistryExportable;
import org.cyclops.cyclopscore.metadata.RegistryExportableRecipeAbstract;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;

/**
 * Environmental accumulator recipe exporter.
 */
public class RegistryExportableEnvirAccRecipe extends RegistryExportableRecipeAbstract<RecipeType<RecipeEnvironmentalAccumulator>, RecipeEnvironmentalAccumulator, RecipeEnvironmentalAccumulator.Inventory> {

    public RegistryExportableEnvirAccRecipe() {
        super(() -> RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR);
    }

    @Override
    public JsonObject serializeRecipe(RecipeEnvironmentalAccumulator recipe) {
        return serializeRecipeStatic(recipe);
    }

    public static JsonObject serializeRecipeStatic(RecipeEnvironmentalAccumulator recipe) {
        JsonObject object = new JsonObject();

        // Recipe object
        ItemStack[] inputItems = recipe.getInputIngredient().getItems();
        JsonArray arrayInputs = new JsonArray();
        for (ItemStack input : inputItems) {
            arrayInputs.add(IRegistryExportable.serializeItemStack(input));
        }
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getOutputItemFirst()));
        object.addProperty("inputWeather", recipe.getInputWeather().toString());
        object.addProperty("outputWeather", recipe.getOutputWeather().toString());
        object.addProperty("duration", recipe.getDuration());
        int amount = AccumulateItemTickAction.getUsage(recipe.getCooldownTime());
        FluidStack fluidStack = new FluidStack(RegistryEntries.FLUID_BLOOD, amount);
        object.add("fluid", IRegistryExportable.serializeFluidStack(fluidStack));

        return object;
    }

}
