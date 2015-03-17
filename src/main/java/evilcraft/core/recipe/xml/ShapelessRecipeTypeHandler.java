package evilcraft.core.recipe.xml;

import com.google.common.collect.Lists;
import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Handler for shapeless recipes.
 * @author rubensworks
 *
 */
public class ShapelessRecipeTypeHandler extends GridRecipeTypeHandler {

	@Override
	protected void handleIO(Element input, ItemStack output)
			throws XmlRecipeException {
		List<Object> inputs = Lists.newLinkedList();
		NodeList inputNodes = input.getElementsByTagName("item");
		for(int i = 0; i < inputNodes.getLength(); i++) {
			inputs.add(getItem(inputNodes.item(i)));
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, inputs.toArray()));
	}

}
