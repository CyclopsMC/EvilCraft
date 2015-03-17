package evilcraft.core.recipe.xml;

/**
 * Item type handler for ore dictionary keys.
 * @author rubensworks
 */
public class OreDictItemTypeHandler extends DefaultItemTypeHandler {

	@Override
	protected Object makeItemStack(String key, int amount, int meta) {
        return key;
    }
	
}
