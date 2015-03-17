package evilcraft.core.recipe.xml;

import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import net.minecraft.item.ItemStack;

/**
 * Item type handler for ore dictionary keys.
 * @author rubensworks
 */
public class PredefinedItemTypeHandler extends DefaultItemTypeHandler {

	@Override
	protected Object makeItemStack(String key, int amount, int meta) throws XmlRecipeException {
        ItemStack item = XmlRecipeLoader.getPredefinedItem(key);
        if(item == null) {
        	throw new XmlRecipeException(String.format(
        			"Could not find the predefined item for key '%s'.", key));
        }
        return item;
    }
	
}
