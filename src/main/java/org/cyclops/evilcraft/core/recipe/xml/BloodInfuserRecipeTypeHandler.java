package org.cyclops.evilcraft.core.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.xml.SuperRecipeTypeHandler;
import org.cyclops.cyclopscore.recipe.xml.XmlRecipeLoader;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handler for blood infuser recipes.
 * @author rubensworks
 *
 */
public class BloodInfuserRecipeTypeHandler extends SuperRecipeTypeHandler<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> {

    @Override
    public String getCategoryId() {
        return Reference.MOD_ID + ":bloodInfuserRecipe";
    }

	@Override
	protected IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> handleRecipe(RecipeHandler recipeHandler, Element input, Element output, Element properties)
			throws XmlRecipeLoader.XmlRecipeException {
		Node inputItem = input.getElementsByTagName("item").item(0);
		Node outputItem = output.getElementsByTagName("item").item(0);
		int duration = Integer.parseInt(properties.getElementsByTagName("duration").item(0).getTextContent());
        int tier = 0;
        if(properties.getElementsByTagName("tier").getLength() > 0) {
            tier = Integer.parseInt(properties.getElementsByTagName("tier").item(0).getTextContent());
        }
        float xp = 0;
        if(properties.getElementsByTagName("xp").getLength() > 0) {
            xp = Float.parseFloat(properties.getElementsByTagName("xp").item(0).getTextContent());
        }

        Object item = getItem(recipeHandler, inputItem);
        FluidStack fluidStack = getFluid(recipeHandler, input.getElementsByTagName("fluid").item(0));
        ItemFluidStackAndTierRecipeComponent recipeComponent;
        if(item instanceof ItemStack) {
            recipeComponent = new ItemFluidStackAndTierRecipeComponent(
                    (ItemStack) item,
                    fluidStack,
                    tier
            );
        } else {
            recipeComponent = new ItemFluidStackAndTierRecipeComponent(
                    (String) item,
                    fluidStack,
                    tier
            );
        }

        ItemStack outputStack = (ItemStack) getItem(recipeHandler, outputItem);
		return BloodInfuser.getInstance().getRecipeRegistry().registerRecipe(
                recipeComponent,
                new ItemStackRecipeComponent(outputStack),
                new DurationXpRecipeProperties(duration, xp)
        );
	}

}
