package evilcraft.core.recipe.xml;

import evilcraft.block.BloodInfuser;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        int tier = 0;
        if(properties.getElementsByTagName("tier").getLength() > 0) {
            tier = Integer.parseInt(properties.getElementsByTagName("tier").item(0).getTextContent());
        }
		
		Fluid fluid = FluidRegistry.getFluid(inputFluid);
		if(fluid == null) {
			throw new XmlRecipeException(String.format("Fluid by name '%s' has not been found.", inputFluid));
		}
		
		BloodInfuser.getInstance().getRecipeRegistry().registerRecipe(
                new ItemFluidStackAndTierRecipeComponent(
                        (ItemStack) getItem(inputItem),
                        new FluidStack(fluid, inputAmount),
                        tier
                ),
                new ItemStackRecipeComponent((ItemStack) getItem(outputItem)),
                new DurationRecipeProperties(duration)
        );
	}

}
