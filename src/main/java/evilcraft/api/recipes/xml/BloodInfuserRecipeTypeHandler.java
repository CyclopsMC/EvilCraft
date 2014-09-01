package evilcraft.api.recipes.xml;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import evilcraft.api.recipes.DurationRecipeProperties;
import evilcraft.api.recipes.ItemAndFluidStackRecipeComponent;
import evilcraft.api.recipes.ItemStackRecipeComponent;
import evilcraft.api.recipes.xml.XmlRecipeLoader.XmlRecipeException;
import evilcraft.blocks.BloodInfuser;

/**
 * Handler for blood infuser recipes.
 * @author rubensworks
 *
 */
public class BloodInfuserRecipeTypeHandler extends SuperRecipeTypeHandler {

	@Override
	protected void handleRecipe(Element input, Element output, Element properties)
			throws XmlRecipeException {
		Node inputItem = input.getElementsByTagName("item").item(0);
		String inputFluid = input.getElementsByTagName("fluid").item(0).getTextContent();
		int inputAmount = Integer.parseInt(input.getElementsByTagName("fluidamount").item(0).getTextContent());
		Node outputItem = output.getElementsByTagName("item").item(0);
		int duration = Integer.parseInt(properties.getElementsByTagName("duration").item(0).getTextContent());
		
		Fluid fluid = FluidRegistry.getFluid(inputFluid);
		if(fluid == null) {
			throw new XmlRecipeException(String.format("Fluid by name '%s' has not been found.", inputFluid));
		}
		
		BloodInfuser.getInstance().getRecipeRegistry().registerRecipe(
                new ItemAndFluidStackRecipeComponent(
                        (ItemStack) getItem(inputItem),
                        new FluidStack(fluid, inputAmount)
                ),
                new ItemStackRecipeComponent((ItemStack) getItem(outputItem)),
                new DurationRecipeProperties(duration)
        );
	}

}
