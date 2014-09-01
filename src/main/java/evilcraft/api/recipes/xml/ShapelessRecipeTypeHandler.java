package evilcraft.api.recipes.xml;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.recipes.xml.XmlRecipeLoader.XmlRecipeException;

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
