package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
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
public class RecipeFluidContainerCombination extends CustomRecipe {

    private final Ingredient fluidContainer;
    private final int maxCapacity;

    public RecipeFluidContainerCombination(ResourceLocation id, CraftingBookCategory category, Ingredient fluidContainer, int maxCapacity) {
        super(id, category);
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
    public boolean matches(CraftingContainer grid, Level world) {
        return !assemble(grid, world.registryAccess()).isEmpty();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return fluidContainer.getItems()[0];
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventory) {
        NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inventory.getItem(i);
            aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(itemstack));
        }

        return aitemstack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_FLUIDCONTAINER_COMBINATION;
    }

    @Override
    public ItemStack assemble(CraftingContainer grid, RegistryAccess registryAccess) {
        ItemStack output = getResultItem(registryAccess).copy();
        IFluidHandlerItemCapacity fluidHandlerOutput = FluidHelpers.getFluidHandlerItemCapacity(output).orElse(null);

        FluidStack commonFluid = null;
        int totalCapacity = 0;
        int totalContent = 0;
        int inputItems = 0;

        // Loop over the grid and count the total contents and capacity + collect common fluid.
        for(int j = 0; j < grid.getContainerSize(); j++) {
            ItemStack element = grid.getItem(j).copy().split(1);
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
                        totalContent = Helpers.addSafe(totalContent, fluidStack.getAmount() * element.getCount());
                    }
                    totalCapacity = Helpers.addSafe(totalCapacity, fluidHandler.getCapacity() * element.getCount());
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
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }
}
