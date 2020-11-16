package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * recipe for combining dark tanks in a shapeless manner.
 * @author rubensworks
 *
 */
public class RecipeFluidContainerCombination extends SpecialRecipe {

	private final Ingredient fluidContainer;
	private final int maxCapacity;

	public RecipeFluidContainerCombination(ResourceLocation id, Ingredient fluidContainer, int maxCapacity) {
		super(id);
		this.fluidContainer = fluidContainer;
		this.maxCapacity = maxCapacity;
	}

	public Ingredient getFluidContainer() {
		return fluidContainer;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	@Override
	public boolean matches(CraftingInventory grid, World world) {
		return !getCraftingResult(grid).isEmpty();
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return fluidContainer.getMatchingStacks()[0];
	}

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory) {
		NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return aitemstack;
    }

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RegistryEntries.RECIPESERIALIZER_FLUIDCONTAINER_COMBINATION;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory grid) {
		ItemStack output = getRecipeOutput().copy();
		IFluidHandlerItemCapacity fluidHandlerOutput = FluidHelpers.getFluidHandlerItemCapacity(output).orElse(null);
		
		FluidStack commonFluid = null;
		int totalCapacity = 0;
		int totalContent = 0;
		int inputItems = 0;
		
		// Loop over the grid and count the total contents and capacity + collect common fluid.
		for(int j = 0; j < grid.getSizeInventory(); j++) {
			ItemStack element = grid.getStackInSlot(j);
			if(!element.isEmpty()) {
				if(fluidContainer.test(element)) {
					IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(element).orElse(null);
					inputItems++;
					FluidStack fluidStack = FluidHelpers.getFluid(fluidHandler);
					if(!fluidStack.isEmpty()) {
						if(commonFluid == null) {
							commonFluid = fluidStack;
						} else if(!commonFluid.equals(fluidStack)) {
							return ItemStack.EMPTY;
						}
						totalContent = Helpers.addSafe(totalContent, fluidStack.getAmount());
					}
					totalCapacity = Helpers.addSafe(totalCapacity, fluidHandler.getCapacity());
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		
		if(inputItems < 2 || totalCapacity > this.maxCapacity) {
			return ItemStack.EMPTY;
		}
		
		// Set capacity and fill fluid into output.
		fluidHandlerOutput.setCapacity(totalCapacity);
		if(commonFluid != null) {
			fluidHandlerOutput.fill(new FluidStack(commonFluid, totalContent), IFluidHandler.FluidAction.EXECUTE);
		}
		output = fluidHandlerOutput.getContainer();
		
		return output;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}
}
