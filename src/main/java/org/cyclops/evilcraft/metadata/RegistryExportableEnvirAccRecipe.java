package org.cyclops.evilcraft.metadata;

import com.google.gson.JsonObject;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.metadata.IRegistryExportable;
import org.cyclops.cyclopscore.metadata.RegistryExportableRecipeAbstract;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

/**
 * Environmental accumulator recipe exporter.
 */
public class RegistryExportableEnvirAccRecipe extends RegistryExportableRecipeAbstract<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> {

    public RegistryExportableEnvirAccRecipe() {
        super(() -> EnvironmentalAccumulator.getInstance().getRecipeRegistry(), "evir_acc_recipe");
    }

    public static JsonObject serializeRecipeIO(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        JsonObject object = new JsonObject();

        // Recipe object
        object.add("input", IRegistryExportable.serializeItemStack(recipe.getInput().getFirstItemStack()));
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getOutput().getFirstItemStack()));
        object.addProperty("inputWeather", recipe.getInput().getWeatherType().toString());
        object.addProperty("outputWeather", recipe.getOutput().getWeatherType().toString());
        object.addProperty("duration", recipe.getProperties().getDuration());
        int amount = AccumulateItemTickAction.getUsage(recipe.getProperties());
        FluidStack fluidStack = new FluidStack(TileSanguinaryEnvironmentalAccumulator.ACCEPTED_FLUID, amount);
        object.add("fluid", IRegistryExportable.serializeFluidStack(fluidStack));

        return object;
    }

    public JsonObject serializeRecipe(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        return serializeRecipeIO(recipe);
    }

}
