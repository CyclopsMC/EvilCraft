package org.cyclops.evilcraft.core.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.block.DarkTank;
import org.cyclops.evilcraft.block.DarkTankConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.item.BloodExtractor;

/**
 * Recipe for combining blood extractors with dark tanks in a shapeless manner for a larger blood extractor.
 * @author rubensworks
 *
 */
public class BloodExtractorCombinationRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

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
		return !getCraftingResult(grid).isEmpty();
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(BloodExtractor.getInstance());
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
			if(!element.isEmpty()) {
				if(element.getItem() == darkTank) {
					tanks++;
					FluidStack fluidStack = FluidUtil.getFluidContained(element);
					if(fluidStack != null) {
						if(!blood.equals(fluidStack.getFluid())) {
							return ItemStack.EMPTY;
						}
						totalContent = Helpers.addSafe(totalContent, fluidStack.amount);
					}
					totalCapacity = Helpers.addSafe(totalCapacity, FluidHelpers.getFluidHandlerItemCapacity(element).getCapacity());
				} else if(element.getItem() == bloodExtractor) {
					extractors++;
					FluidStack fluidStack = FluidUtil.getFluidContained(element);
					if(fluidStack != null) {
						if(!blood.equals(fluidStack.getFluid())) {
							return ItemStack.EMPTY;
						}
						totalContent = Helpers.addSafe(totalContent, fluidStack.amount);
					}
					IFluidHandlerItemCapacity fluidHandlerElement = FluidHelpers.getFluidHandlerItemCapacity(element);
					totalCapacity = Helpers.addSafe(totalCapacity, fluidHandlerElement.getCapacity());
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		
		if((extractors + tanks) < 2 || extractors < 1
				|| totalCapacity > DarkTankConfig.maxTankSize) {
			return ItemStack.EMPTY;
		}
		
		// Set capacity and fill fluid into output.
		IFluidHandlerItemCapacity fluidHandlerOutput = FluidHelpers.getFluidHandlerItemCapacity(output);
		fluidHandlerOutput.setCapacity(totalCapacity);
		fluidHandlerOutput.fill(new FluidStack(blood, totalContent), true);
		
		return output;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= size;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.withSize(size, Ingredient.fromItem(BloodExtractor.getInstance()));
	}
}
