package evilcraft.core.recipe.xml;

import cpw.mods.fml.common.Loader;

/**
 * Condition handler for checking if mods are available.
 * @author rubensworks
 *
 */
public class ModRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(String param) {
		return Loader.isModLoaded(param);
	}

}
