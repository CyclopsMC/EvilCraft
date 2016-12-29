package org.cyclops.evilcraft.core.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.block.DarkTank;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

/**
 * recipe for combining dark tanks in a shapeless manner.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainerCombinationRecipe implements IRecipe {
	
	private final int size;
	private final ItemBlockFluidContainer tankItem;
	private final int maxTankSize;
	
	/**
	 * Make a new instance.
	 * @param size The recipe size (should be called multiple times (1 to 9) to allow for all shapeless crafting types.
	 * @param tankItem The tank item that is combinable.
	 */
	public ItemBlockFluidContainerCombinationRecipe(int size, ItemBlockFluidContainer tankItem, int maxTankSize) {
		this.size = size;
		this.tankItem = tankItem;
		this.maxTankSize = maxTankSize;
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
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventory) {
		NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return aitemstack;
    }

    @Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {						
		ItemStack output = getRecipeOutput().copy();
		IFluidHandlerItemCapacity fluidHandlerOutput = FluidHelpers.getFluidHandlerItemCapacity(output);
		
		Fluid commonFluid = null;
		int totalCapacity = 0;
		int totalContent = 0;
		int inputItems = 0;
		
		// Loop over the grid and count the total contents and capacity + collect common fluid.
		for(int j = 0; j < grid.getSizeInventory(); j++) {
			ItemStack element = grid.getStackInSlot(j);
			if(!element.isEmpty()) {
				if(element.getItem() == tankItem) {
					IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(element);
					inputItems++;
					FluidStack fluidStack = FluidHelpers.getFluid(fluidHandler);
					if(fluidStack != null) {
						if(commonFluid == null) {
							commonFluid = fluidStack.getFluid();
						} else if(!commonFluid.equals(fluidStack.getFluid())) {
							return null;
						}
						totalContent = Helpers.addSafe(totalContent, fluidStack.amount);
					}
					totalCapacity = Helpers.addSafe(totalCapacity, fluidHandler.getCapacity());
				} else {
					return null;
				}
			}
		}
		
		if(inputItems < 2
				|| totalCapacity > this.maxTankSize) {
			return null;
		}
		
		// Set capacity and fill fluid into output.
		fluidHandlerOutput.setCapacity(totalCapacity);
		if(commonFluid != null) {
			fluidHandlerOutput.fill(new FluidStack(commonFluid, totalContent), true);
		}
		output = fluidHandlerOutput.getContainer();
		
		return output;
	}
	
}
