package evilcraft.core.recipes.xml;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Node;

import cpw.mods.fml.common.registry.GameData;
import evilcraft.core.recipes.xml.XmlRecipeLoader.XmlRecipeException;

/**
 * Default item type handler for text nodes of the form evilcraft:darkGem"
 * @author rubensworks
 */
public class DefaultItemTypeHandler implements IItemTypeHandler {
	
	protected Object makeItemStack(String key, int amount, int meta) throws XmlRecipeException {
        return new ItemStack(GameData.getItemRegistry().getObject(key), amount, meta);
    }
	
	@Override
	public Object getItem(Node itemNode) throws XmlRecipeException {
		String element = itemNode.getTextContent();
		if(element == null || element.isEmpty()) return null;
		
		int amount = 1;
		Node amountNode = itemNode.getAttributes().getNamedItem("amount");
		if(amountNode != null) {
			amount = Integer.parseInt(amountNode.getTextContent());
		}
		
		int meta = 0;
		Node metaNode = itemNode.getAttributes().getNamedItem("meta");
		if(metaNode != null) {
			meta = Integer.parseInt(metaNode.getTextContent());
		}
		
		return makeItemStack(element, amount, meta);
	}

}
