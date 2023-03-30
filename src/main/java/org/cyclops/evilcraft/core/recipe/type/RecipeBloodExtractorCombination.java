package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDarkTank;
import org.cyclops.evilcraft.item.ItemBloodExtractor;

/**
 * Recipe for combining blood extractors with dark tanks in a shapeless manner for a larger blood extractor.
 * @author rubensworks
 *
 */
public class RecipeBloodExtractorCombination extends CustomRecipe {

    private final int maxCapacity;

    public RecipeBloodExtractorCombination(ResourceLocation id, CraftingBookCategory category, int maxCapacity) {
        super(id, category);
        this.maxCapacity = maxCapacity;
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
        return new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
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
        return RegistryEntries.RECIPESERIALIZER_BLOODEXTRACTOR_COMBINATION;
    }

    @Override
    public ItemStack assemble(CraftingContainer grid, RegistryAccess registryAccess) {
        ItemStack output = getResultItem(registryAccess).copy();

        int totalCapacity = 0;
        int totalContent = 0;
        int extractors = 0;
        int tanks = 0;

        // Loop over the grid and count the total contents and capacity
        for(int j = 0; j < grid.getContainerSize(); j++) {
            ItemStack element = grid.getItem(j).copy().split(1);
            if(!element.isEmpty()) {
                if(element.getItem() instanceof BlockItem && ((BlockItem) element.getItem()).getBlock() instanceof BlockDarkTank) {
                    tanks += element.getCount();
                    FluidStack fluidStack = FluidUtil.getFluidContained(element).orElse(FluidStack.EMPTY);
                    if(!fluidStack.isEmpty()) {
                        if(fluidStack.getFluid() != RegistryEntries.FLUID_BLOOD) {
                            return ItemStack.EMPTY;
                        }
                        totalContent = Helpers.addSafe(totalContent, fluidStack.getAmount() * element.getCount());
                    }
                    totalCapacity = Helpers.addSafe(totalCapacity, FluidHelpers.getFluidHandlerItemCapacity(element)
                            .map(IFluidHandlerItemCapacity::getCapacity)
                            .orElse(0) * element.getCount());
                } else if(element.getItem() instanceof ItemBloodExtractor) {
                    extractors += element.getCount();
                    FluidStack fluidStack = FluidUtil.getFluidContained(element).orElse(FluidStack.EMPTY);
                    if(!fluidStack.isEmpty()) {
                        if(fluidStack.getFluid() != RegistryEntries.FLUID_BLOOD) {
                            return ItemStack.EMPTY;
                        }
                        totalContent = Helpers.addSafe(totalContent, fluidStack.getAmount() * element.getCount());
                    }
                    totalCapacity = Helpers.addSafe(totalCapacity, FluidHelpers.getFluidHandlerItemCapacity(element)
                            .map(IFluidHandlerItemCapacity::getCapacity)
                            .orElse(0) * element.getCount());
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if((extractors + tanks) < 2 || extractors < 1
                || totalCapacity > getMaxCapacity()) {
            return ItemStack.EMPTY;
        }

        // Set capacity and fill fluid into output.
        IFluidHandlerItemCapacity fluidHandlerOutput = FluidHelpers.getFluidHandlerItemCapacity(output).orElse(null);
        fluidHandlerOutput.setCapacity(totalCapacity);
        fluidHandlerOutput.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, totalContent), IFluidHandler.FluidAction.EXECUTE);

        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }
}
