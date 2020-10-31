package org.cyclops.evilcraft.core.recipe;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Recipe for combining blood extractors with dark tanks in a shapeless manner for a larger blood extractor.
 * @author rubensworks
 *
 */
public class RecipeDisplayStand extends ShapedRecipe {

	public RecipeDisplayStand(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn,
							  NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inventoryCrafting) {
		ItemStack plankWoodStack = ItemStack.EMPTY;
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
			for (Item plankType : ItemTags.PLANKS.getAllElements()) {
				ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
				if (!itemStack.isEmpty() && itemStack.getItem() == plankType) {
					plankWoodStack = itemStack;
				}
			}
		}
		if (plankWoodStack.isEmpty()) {
			return null;
		}
		BlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack);
		return RegistryEntries.BLOCK_DISPLAY_STAND.getTypedDisplayStandItem(plankWoodBlockState);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RegistryEntries.RECIPESERIALIZER_DISPLAY_STAND;
	}
}
