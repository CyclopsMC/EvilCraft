package evilcraft.core.recipe.xml;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Recipe type handler for smelting recipes.
 * @author rubensworks
 */
public class SmeltingRecipeTypeHandler extends CommonRecipeTypeHandler {

	@Override
	public ItemStack loadRecipe(Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack inputItem = (ItemStack) getItem(input.getElementsByTagName("item").item(0));
		ItemStack outputItem = (ItemStack) getItem(output.getElementsByTagName("item").item(0));
		int xp = 0;
		if(output.getElementsByTagName("xp").getLength() > 0) {
			xp = Integer.parseInt(output.getElementsByTagName("xp").item(0).getTextContent());
		}
		
		GameRegistry.addSmelting(inputItem, outputItem, xp);
        return outputItem;
	}

}
