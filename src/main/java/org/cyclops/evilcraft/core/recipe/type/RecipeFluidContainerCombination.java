package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * recipe for combining dark tanks in a shapeless manner.
 * @author rubensworks
 *
 */
public class RecipeFluidContainerCombination extends CustomRecipe {

    private final Ingredient fluidContainer;
    private final int maxCapacity;

    public RecipeFluidContainerCombination(CraftingBookCategory category, Ingredient fluidContainer, int maxCapacity) {
        super(category);
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
    public boolean matches(CraftingInput grid, Level world) {
        return !assemble(grid, world.registryAccess()).isEmpty();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inventory.getItem(i);
            aitemstack.set(i, itemstack.getCraftingRemainder());
        }

        return aitemstack;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_FLUIDCONTAINER_COMBINATION.get();
    }

    @Override
    public ItemStack assemble(CraftingInput grid, HolderLookup.Provider registryAccess) {
        ItemStack output = new ItemStack(fluidContainer.items().findFirst().get());
        IFluidHandlerItemCapacity fluidHandlerOutput = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(output).orElse(null);

        FluidStack commonFluid = null;
        int totalCapacity = 0;
        int totalContent = 0;
        int inputItems = 0;

        // Loop over the grid and count the total contents and capacity + collect common fluid.
        for(int j = 0; j < grid.size(); j++) {
            ItemStack element = grid.getItem(j).copy().split(1);
            if(!element.isEmpty()) {
                if(fluidContainer.test(element)) {
                    IFluidHandlerItemCapacity fluidHandler = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(element).orElse(null);
                    inputItems++;
                    FluidStack fluidStack = IModHelpersNeoForge.get().getFluidHelpers().getFluid(fluidHandler);
                    if(!fluidStack.isEmpty()) {
                        if(commonFluid == null) {
                            commonFluid = fluidStack;
                        } else if(!commonFluid.equals(fluidStack)) {
                            return ItemStack.EMPTY;
                        }
                        totalContent = IModHelpers.get().getBaseHelpers().addSafe(totalContent, fluidStack.getAmount() * element.getCount());
                    }
                    totalCapacity = IModHelpers.get().getBaseHelpers().addSafe(totalCapacity, fluidHandler.getCapacity() * element.getCount());
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
            fluidHandlerOutput.fill(new FluidStack(commonFluid.getFluidHolder(), totalContent), IFluidHandler.FluidAction.EXECUTE);
        }
        output = fluidHandlerOutput.getContainer();

        return output;
    }
}
