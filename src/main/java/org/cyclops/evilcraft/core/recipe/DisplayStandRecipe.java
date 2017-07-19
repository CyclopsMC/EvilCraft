package org.cyclops.evilcraft.core.recipe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.DisplayStand;

import java.util.List;

/**
 * Recipe for combining blood extractors with dark tanks in a shapeless manner for a larger blood extractor.
 * @author rubensworks
 *
 */
public class DisplayStandRecipe extends ShapedOreRecipe {

	private final List<ItemStack> plankTypes;

	public DisplayStandRecipe(List<ItemStack> plankTypes) {
		super(new ResourceLocation(Reference.MOD_ID, "display_stand"), DisplayStand.getInstance().
						getTypedDisplayStandItem(Blocks.PLANKS.getDefaultState()),
				"SBS", "SPS", " P ",
				'S', Reference.DICT_WOODSTICK,
				'B', "slabWood",
				'P', Reference.DICT_WOODPLANK);
		this.plankTypes = plankTypes;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
		ItemStack plankWoodStack = ItemStack.EMPTY;
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
			for (ItemStack plankType : plankTypes) {
				ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
				if (!itemStack.isEmpty() && OreDictionary.itemMatches(plankType, itemStack, false)) {
					plankWoodStack = itemStack;
				}
			}
		}
		if (plankWoodStack.isEmpty()) {
			return null;
		}
		IBlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack);
		return DisplayStand.getInstance().
				getTypedDisplayStandItem(plankWoodBlockState);
	}
}
