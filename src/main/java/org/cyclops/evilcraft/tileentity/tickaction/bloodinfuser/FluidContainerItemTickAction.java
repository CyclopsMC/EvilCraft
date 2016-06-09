package org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;

/**
 * {@link ITickAction} that can fill fluid containers with blood.
 * @author rubensworks
 *
 */
public class FluidContainerItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        ItemStack infuseStack = getInfuseStack(tile);
        IFluidHandler container = FluidUtil.getFluidHandler(infuseStack);
        FluidStack fluidStack = tile.getTank().getFluid().copy();

        MutableInt duration = new MutableInt(MB_PER_TICK);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_FILLBLOODPERTICK));
        int minAmount = duration.getValue();

        fluidStack.amount = Math.min(minAmount, fluidStack.amount);
        int filled = container.fill(fluidStack, true);
        if (filled > 0) {
            // Everything ok, filling the container bit by bit
            tile.getTank().drain(filled, true);
            if (FluidHelpers.getFluid(container) != null && FluidHelpers.getAmount(FluidHelpers.getFluid(container)) == FluidHelpers.getCapacity(container)) {
                if (addToProduceSlot(tile, infuseStack)) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                }
            }
        } else {
            // We might be dealing with a bucket
            ItemStack result = FluidUtil.tryFillContainer(infuseStack, tile.getTank(), Integer.MAX_VALUE, null, true);
            if (addToProduceSlot(tile, result)) {
                tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
            }
        }
    }

    @Override
    public int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot) {
        return EmptyFluidContainerInTankTickAction.getRequiredTicks(tile, getInfuseStack(tile));
    }

    @Override
    public ItemStack willProduceItem(TileBloodInfuser tile) {
        ItemStack itemStack = tile.getInventory().getStackInSlot(tile.getConsumeSlot());
        if (itemStack == null) {
            return null;
        }
        ItemStack smallContainer = FluidUtil.tryFillContainer(itemStack, tile.getTank(), MB_PER_TICK, null, false);
        if (smallContainer != null) {
            return smallContainer;
        }
        return FluidUtil.tryFillContainer(itemStack, tile.getTank(), Fluid.BUCKET_VOLUME, null, false);
    }
    
}
