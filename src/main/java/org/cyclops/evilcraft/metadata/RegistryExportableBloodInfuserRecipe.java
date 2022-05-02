package org.cyclops.evilcraft.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.metadata.IRegistryExportable;
import org.cyclops.cyclopscore.metadata.RegistryExportableRecipeAbstract;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.recipe.type.IInventoryFluidTier;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;

/**
 * Blood infuser recipe exporter.
 */
public class RegistryExportableBloodInfuserRecipe extends RegistryExportableRecipeAbstract<RecipeType<RecipeBloodInfuser>, RecipeBloodInfuser, IInventoryFluidTier> {

    protected RegistryExportableBloodInfuserRecipe() {
        super(() -> RegistryEntries.RECIPETYPE_BLOOD_INFUSER);
    }

    @Override
    public JsonObject serializeRecipe(RecipeBloodInfuser recipe) {
        return serializeRecipeStatic(recipe);
    }

    public static JsonObject serializeRecipeStatic(RecipeBloodInfuser recipe) {
        JsonObject object = new JsonObject();

        // Properties
        object.addProperty("tier", recipe.getInputTier());
        object.addProperty("duration", recipe.getDuration());
        object.addProperty("xp", recipe.getXp());

        // Inputs
        JsonObject inputObject = new JsonObject();
        ItemStack[] inputItems = recipe.getInputIngredient().getItems();
        JsonArray arrayInputs = new JsonArray();
        for (ItemStack input : inputItems) {
            arrayInputs.add(IRegistryExportable.serializeItemStack(input));
        }
        inputObject.add("item", arrayInputs);
        FluidStack inputFluid = recipe.getInputFluid();
        if (inputFluid != null) {
            inputObject.add("fluid", IRegistryExportable.serializeFluidStack(inputFluid));
        }

        // Outputs
        JsonObject outputObject = new JsonObject();
        ItemStack itemOutput = recipe.getOutputItemFirst();
        outputObject.add("item", IRegistryExportable.serializeItemStack(itemOutput));

        // Recipe object
        object.add("input", inputObject);
        object.add("output", outputObject);

        return object;
    }

}
