package org.cyclops.evilcraft.modcompat.jei;

import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class SubtypeInterpreterActivatableFluidContainer implements ISubtypeRegistry.ISubtypeInterpreter {
    @Nullable
    @Override
    public String getSubtypeInfo(@Nonnull ItemStack itemStack) {
        return "";
        /*FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);
        if (fluidStack.amount == 0) fluidStack = null;
        return (ItemHelpers.isActivated(itemStack) ? "true" : "false") + "::" + (fluidStack != null ? fluidStack.getFluid().getUnlocalizedName() : "none");*/
    }
}
