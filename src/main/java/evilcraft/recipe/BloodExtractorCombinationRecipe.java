package evilcraft.recipe;

import evilcraft.block.DarkTank;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.fluid.Blood;
import evilcraft.item.BloodExtractor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Recipe for combining blood extractors with dark tanks in a shapeless manner for a larger blood extractor.
 * @author rubensworks
 *
 */
public class BloodExtractorCombinationRecipe implements IRecipe {

	private int size;

	/**
	 * Make a new instance.
	 * @param size The recipe size (should be called multiple times (1 to 9) to allow for all shapeless crafting types.
	 */
	public BloodExtractorCombinationRecipe(int size) {
		this.size = size;
	}

	@Override
	public boolean matches(InventoryCrafting grid, World world) {
		return getCraftingResult(grid) != null;
	}
	
	@Override
	public int getRecipeSize() {
		return size;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(BloodExtractor.getInstance());
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {						
		ItemStack output = getRecipeOutput().copy();

		BloodExtractor bloodExtractor = BloodExtractor.getInstance();
		ItemBlockFluidContainer darkTank = (ItemBlockFluidContainer) Item.getItemFromBlock(DarkTank.getInstance());
		Fluid blood = Blood.getInstance();

		int totalCapacity = 0;
		int totalContent = 0;
		int extractors = 0;
		int tanks = 0;
		
		// Loop over the grid and count the total contents and capacity
		for(int j = 0; j < grid.getSizeInventory(); j++) {
			ItemStack element = grid.getStackInSlot(j);
			if(element != null) {
				if(element.getItem() == darkTank) {
					tanks++;
					FluidStack fluidStack = darkTank.getFluid(element);
					if(fluidStack != null) {
						if(!blood.equals(fluidStack.getFluid())) {
							return null;
						}
						totalContent += fluidStack.amount;
					}
					totalCapacity += darkTank.getCapacity(element);
				} else if(element.getItem() == bloodExtractor) {
					extractors++;
					FluidStack fluidStack = bloodExtractor.getFluid(element);
					if(fluidStack != null) {
						if(!blood.equals(fluidStack.getFluid())) {
							return null;
						}
						totalContent += fluidStack.amount;
					}
					totalCapacity += bloodExtractor.getCapacity(element);
				} else {
					return null;
				}
			}
		}
		
		if((extractors + tanks) < 2 || extractors < 1
				|| totalCapacity > darkTank.getBlockTank().getMaxCapacity()) {
			return null;
		}
		
		// Set capacity and fill fluid into output.
		bloodExtractor.setCapacity(output, totalCapacity);
		bloodExtractor.fill(output, new FluidStack(blood, totalContent), true);
		
		return output;
	}
	
}
