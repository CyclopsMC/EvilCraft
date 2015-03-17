package evilcraft.core.recipe.xml;

import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;
import org.w3c.dom.Node;

/**
 * Default item type handler for text nodes of the form evilcraft:darkGem"
 * @author rubensworks
 */
public class DefaultItemTypeHandler implements IItemTypeHandler {
	
	protected Object makeItemStack(String key, int amount, int meta) throws XmlRecipeException {
        Item item = GameData.getItemRegistry().getObject(key);
        if(item == null) {
            throw new XmlRecipeException(String.format("Item by name '%s' has not been found.", key));
        }
        return new ItemStack(item, amount, meta);
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
            if("*".equals(metaNode.getTextContent())) {
                meta = OreDictionary.WILDCARD_VALUE;
            } else {
                meta = Integer.parseInt(metaNode.getTextContent());
            }
		}
		
		return makeItemStack(element, amount, meta);
	}

}
