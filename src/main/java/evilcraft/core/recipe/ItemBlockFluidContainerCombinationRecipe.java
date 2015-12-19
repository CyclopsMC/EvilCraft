package evilcraft.core.recipe;

import evilcraft.block.DarkTank;
import evilcraft.core.helper.Helpers;
import evilcraft.core.item.ItemBlockFluidContainer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * recipe for combining dark tanks in a shapeless manner.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainerCombinationRecipe implements IRecipe {
	
	private int size;
	private ItemBlockFluidContainer tankItem;
	
	/**
	 * Make a new instance.
	 * @param size The recipe size (should be called multiple times (1 to 9) to allow for all shapeless crafting types.
	 * @param tankItem The tank item that is combinable.
	 */
	public ItemBlockFluidContainerCombinationRecipe(int size, ItemBlockFluidContainer tankItem) {
		this.size = size;
		this.tankItem = tankItem;
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
		return new ItemStack(DarkTank.getInstance());
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {						
		ItemStack output = getRecipeOutput().copy();
		
		Fluid commonFluid = null;
		int totalCapacity = 0;
		int totalContent = 0;
		int inputItems = 0;
		
		// Loop over the grid and count the total contents and capacity + collect common fluid.
		for(int j = 0; j < grid.getSizeInventory(); j++) {
			ItemStack element = grid.getStackInSlot(j);
			if(element != null) {
				if(element.getItem() == tankItem) {
					inputItems++;
					FluidStack fluidStack = tankItem.getFluid(element);
					if(fluidStack != null) {
						if(commonFluid == null) {
							commonFluid = fluidStack.getFluid();
						} else if(!commonFluid.equals(fluidStack.getFluid())) {
							return null;
						}
						totalContent = Helpers.addSafe(totalContent, fluidStack.amount);
					}
					totalCapacity = Helpers.addSafe(totalCapacity, tankItem.getCapacity(element));
				} else {
					return null;
				}
			}
		}
		
		if(inputItems < 2
				|| totalCapacity > tankItem.getBlockTank().getMaxCapacity()) {
			return null;
		}
		
		// Set capacity and fill fluid into output.
		tankItem.setCapacity(output, totalCapacity);
		if(commonFluid != null) {
			tankItem.fill(output, new FluidStack(commonFluid, totalContent), true);
		}
		
		return output;
	}
	
}
