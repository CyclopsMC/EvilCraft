package org.cyclops.evilcraft.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.metadata.IRegistryExportable;
import org.cyclops.cyclopscore.metadata.RegistryExportableRecipeAbstract;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

/**
 * Environmental accumulator recipe exporter.
 */
public class RegistryExportableEnvirAccRecipe extends RegistryExportableRecipeAbstract<IRecipeType<RecipeEnvironmentalAccumulator>, RecipeEnvironmentalAccumulator, RecipeEnvironmentalAccumulator.Inventory> {

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
        ItemStack[] inputItems = recipe.getInputIngredient().getMatchingStacks();
        JsonArray arrayInputs = new JsonArray();
        for (ItemStack input : inputItems) {
            arrayInputs.add(IRegistryExportable.serializeItemStack(input));
        }
        object.add("input", arrayInputs);
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getOutputItem()));
        object.addProperty("inputWeather", recipe.getInputWeather().toString());
        object.addProperty("outputWeather", recipe.getOutputWeather().toString());
        object.addProperty("duration", recipe.getDuration());
        int amount = AccumulateItemTickAction.getUsage(recipe.getCooldownTime());
        FluidStack fluidStack = new FluidStack(RegistryEntries.FLUID_BLOOD, amount);
        object.add("fluid", IRegistryExportable.serializeFluidStack(fluidStack));

        return object;
    }

}
