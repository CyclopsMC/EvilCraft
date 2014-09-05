package evilcraft.core.recipes.events;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * Observer interface of {@link ObservableShapedRecipe}.
 * @author rubensworks
 *
 */
public interface IRecipeOutputObserver {

	/**
	 * Called when a recipe output is asked from a recipe.
	 * @param craftingGrid The input crafting grid.
	 * @param output The original recipe output.
	 * @return An optionally different output.
	 */
	public ItemStack getRecipeOutput(InventoryCrafting craftingGrid, ItemStack output);
	
}
