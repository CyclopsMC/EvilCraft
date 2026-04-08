package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
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

    public RecipeBloodExtractorCombination(CraftingBookCategory category, int maxCapacity) {
        super();
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public boolean matches(CraftingInput grid, Level world) {
        return !assemble(grid).isEmpty();
    }

    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inventory.getItem(i);
            net.minecraft.world.item.ItemStackTemplate remainder = itemstack.getCraftingRemainder();
            aitemstack.set(i, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return aitemstack;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BLOODEXTRACTOR_COMBINATION.get();
    }

    @Override
    public ItemStack assemble(CraftingInput grid) {
        ItemStack output = getResultItem(null).copy();

        int totalCapacity = 0;
        int totalContent = 0;
        int extractors = 0;
        int tanks = 0;

        // Loop over the grid and count the total contents and capacity
        for(int j = 0; j < grid.size(); j++) {
            ItemStack element = grid.getItem(j).copy().split(1);
            if(!element.isEmpty()) {
                if(element.getItem() instanceof BlockItem && ((BlockItem) element.getItem()).getBlock() instanceof BlockDarkTank) {
                    tanks += element.getCount();
                    FluidStack fluidStack = FluidUtil.getFirstStackContained(element);
                    if(!fluidStack.isEmpty()) {
                        if(fluidStack.getFluid() != RegistryEntries.FLUID_BLOOD.get()) {
                            return ItemStack.EMPTY;
                        }
                        totalContent = IModHelpers.get().getBaseHelpers().addSafe(totalContent, fluidStack.getAmount() * element.getCount());
                    }
                    totalCapacity = IModHelpers.get().getBaseHelpers().addSafe(totalCapacity, IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(element))
                            .map(h -> h.getTankCapacity(0))
                            .orElse(0) * element.getCount());
                } else if(element.getItem() instanceof ItemBloodExtractor) {
                    extractors += element.getCount();
                    FluidStack fluidStack = FluidUtil.getFirstStackContained(element);
                    if(!fluidStack.isEmpty()) {
                        if(fluidStack.getFluid() != RegistryEntries.FLUID_BLOOD.get()) {
                            return ItemStack.EMPTY;
                        }
                        totalContent = IModHelpers.get().getBaseHelpers().addSafe(totalContent, fluidStack.getAmount() * element.getCount());
                    }
                    totalCapacity = IModHelpers.get().getBaseHelpers().addSafe(totalCapacity, IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(element))
                            .map(h -> h.getTankCapacity(0))
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
        ItemAccess outputItemAccess = ItemAccess.forStack(output);
        IFluidHandlerCapacity fluidHandlerOutput = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(outputItemAccess).orElse(null);
        try (var tx = Transaction.openRoot()) {
            fluidHandlerOutput.setTankCapacity(0, totalCapacity, tx);
            fluidHandlerOutput.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), totalContent, tx);
            tx.commit();
        }

        return outputItemAccess.getResource().toStack(outputItemAccess.getAmount());
    }
}
