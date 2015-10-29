package org.cyclops.evilcraft.core.recipe.xml;

import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.xml.SuperRecipeTypeHandler;
import org.cyclops.cyclopscore.recipe.xml.XmlRecipeLoader;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handler for blood infuser recipes.
 * @author rubensworks
 *
 */
public class BloodInfuserRecipeTypeHandler extends SuperRecipeTypeHandler {

	@Override
	protected ItemStack handleRecipe(RecipeHandler recipeHandler, Element input, Element output, Element properties)
			throws XmlRecipeLoader.XmlRecipeException {
		Node inputItem = input.getElementsByTagName("item").item(0);
		String inputFluid = input.getElementsByTagName("fluid").item(0).getTextContent();
		int inputAmount = Integer.parseInt(input.getElementsByTagName("fluidamount").item(0).getTextContent());
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
		
		Fluid fluid = FluidRegistry.getFluid(inputFluid);
		if(fluid == null) {
			throw new XmlRecipeLoader.XmlRecipeException(String.format("Fluid by name '%s' has not been found.", inputFluid));
		}

        Object item = getItem(recipeHandler, inputItem);
        ItemFluidStackAndTierRecipeComponent recipeComponent;
        if(item instanceof ItemStack) {
            recipeComponent = new ItemFluidStackAndTierRecipeComponent(
                    (ItemStack) item,
                    new FluidStack(fluid, inputAmount),
                    tier
            );
        } else {
            recipeComponent = new ItemFluidStackAndTierRecipeComponent(
                    (String) item,
                    new FluidStack(fluid, inputAmount),
                    tier
            );
        }

        ItemStack outputStack = (ItemStack) getItem(recipeHandler, outputItem);
		BloodInfuser.getInstance().getRecipeRegistry().registerRecipe(
                recipeComponent,
                new ItemStackRecipeComponent(outputStack),
                new DurationXpRecipeProperties(duration, xp)
        );
        return outputStack;
	}

}
