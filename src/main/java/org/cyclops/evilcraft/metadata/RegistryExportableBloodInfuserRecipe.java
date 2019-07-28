package org.cyclops.evilcraft.metadata;

import com.google.gson.JsonObject;
import org.cyclops.cyclopscore.metadata.IRegistryExportable;
import org.cyclops.cyclopscore.metadata.RegistryExportableRecipeAbstract;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.IngredientFluidStackAndTierRecipeComponent;

/**
 * Blood infuser recipe exporter.
 */
public class RegistryExportableBloodInfuserRecipe extends RegistryExportableRecipeAbstract<IngredientFluidStackAndTierRecipeComponent, IngredientRecipeComponent, DurationXpRecipeProperties> {

    public RegistryExportableBloodInfuserRecipe() {
        super(() -> BloodInfuser.getInstance().getRecipeRegistry(), "blood_infuser_recipe");
    }

    public static JsonObject serializeRecipeIO(IRecipe<IngredientFluidStackAndTierRecipeComponent, IngredientRecipeComponent, DurationXpRecipeProperties> recipe) {
        JsonObject object = new JsonObject();

        // Recipe object
        object.add("inputItem", IRegistryExportable.serializeItemStack(recipe.getInput().getFirstItemStack()));
        object.add("inputFluid", IRegistryExportable.serializeFluidStack(recipe.getInput().getFluidStack()));
        object.add("output", IRegistryExportable.serializeItemStack(recipe.getOutput().getFirstItemStack()));
        object.addProperty("tier", recipe.getInput().getTier());
        object.addProperty("duration", recipe.getProperties().getDuration());
        object.addProperty("xp", recipe.getProperties().getXp());

        return object;
    }

    public JsonObject serializeRecipe(IRecipe<IngredientFluidStackAndTierRecipeComponent, IngredientRecipeComponent, DurationXpRecipeProperties> recipe) {
        return serializeRecipeIO(recipe);
    }

}
