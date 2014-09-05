package evilcraft.core.recipe.event;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 * A shaped recipe that is observable in terms of the recipe output.
 * The observer can update the output if it desires to do so.
 * @author rubensworks
 *
 */
public class ObservableShapedRecipe extends ShapedRecipes {
	
	private IRecipeOutputObserver observer;

	/**
	 * Make a new instance.
	 * @param recipeWidth The recipe width.
	 * @param recipeHeight The recipe height.
	 * @param recipeItems The recipe items.
	 * @param recipeOutput The recipe output.
	 * @param observer The observer for the output.
	 */
	public ObservableShapedRecipe(int recipeWidth, int recipeHeight,
			ItemStack[] recipeItems, ItemStack recipeOutput, IRecipeOutputObserver observer) {
		super(recipeWidth, recipeHeight, recipeItems, recipeOutput);
		this.observer = observer;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftingGrid) {
		return observer.getRecipeOutput(craftingGrid, super.getCraftingResult(craftingGrid));
    }

}
