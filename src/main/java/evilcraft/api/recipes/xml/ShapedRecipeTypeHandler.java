package evilcraft.api.recipes.xml;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.recipes.xml.XmlRecipeLoader.XmlRecipeException;


/**
 * Handler for shaped recipes.
 * @author rubensworks
 *
 */
public class ShapedRecipeTypeHandler extends GridRecipeTypeHandler {

	@Override
	protected void handleIO(Element input, ItemStack output) throws XmlRecipeException {		
		Element inputGrid = (Element) input.getElementsByTagName("grid").item(0);
		NodeList gridRows = inputGrid.getElementsByTagName("gridrow");
		
		// First valid character for recipes.
        char parameterCounter = 65;
        
        // The recipe lines. These contain the three recipe box lines and the KV mapping
        // of character key as used in the recipe lines and the item ID.
        List<Object> lines = Lists.newLinkedList();
        Map<Object, Character> parameters = Maps.newHashMap(); // key to char
		
        // Add the three recipe box lines.
		for(int row = 0; row < gridRows.getLength(); row++) {
			String parameterLine = "";
			Element gridRow = (Element) gridRows.item(row);
			NodeList gridColumns = gridRow.getElementsByTagName("item");
			for(int col = 0; col < gridColumns.getLength(); col++) {
				Node gridColumn = gridColumns.item(col);
				Object item = getItem(gridColumn);
				if(item == null) {
					parameterLine += " ";
				} else {
					if(!parameters.containsKey(item)) {
                        parameters.put(item, parameterCounter++);
                    }
                    char parameter = parameters.get(item);
                    parameterLine += parameter;
				}
			}
			lines.add(parameterLine);
		}
		
		// Add the pairs of character key and item id.
        for(Entry<Object, Character> entry : parameters.entrySet()) {
            lines.add(entry.getValue());
            if(entry.getKey() == null) {
                throw new XmlRecipeException(String.format(
                		"The recipe %s -> %s has an invalid structure.",
                		input.toString(), output.toString()));
            }
            lines.add(entry.getKey());
        }
        
        // Register with the recipe lines we just constructed.
        GameRegistry.addRecipe(new ShapedOreRecipe(output, true, lines.toArray()));
	}

}
