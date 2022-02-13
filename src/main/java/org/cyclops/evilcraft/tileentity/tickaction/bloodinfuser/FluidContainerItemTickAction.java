package org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.core.fluid.FluidContainerItemWrapperWithSimulation;
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
    public boolean canTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        return super.canTick(tile, itemStack, slot, tick) && FluidUtil.getFluidHandler(itemStack).isPresent();
    }

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        ItemStack infuseStack = getInfuseStack(tile);
        if (!infuseStack.isEmpty()) {
            infuseStack = infuseStack.copy();
        }
        IFluidHandler container = FluidUtil.getFluidHandler(infuseStack).orElse(null);
        FluidStack fluidStack = tile.getTank().getFluid().copy();

        MutableInt duration = new MutableInt(MB_PER_TICK);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_FILLBLOODPERTICK));
        int minAmount = duration.getValue();

        fluidStack.setAmount(Math.min(minAmount, fluidStack.getAmount()));
        int filled = container.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        if (filled > 0) {
            // Everything ok, filling the container bit by bit
            tile.getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
            tile.getInventory().setItem(tile.getTileWorkingMetadata().getConsumeSlot(), infuseStack);
            if (!FluidHelpers.getFluid(container).isEmpty() && FluidHelpers.getAmount(FluidHelpers.getFluid(container)) == FluidHelpers.getCapacity(container)) {
                if (addToProduceSlot(tile, infuseStack)) {
                    tile.getInventory().removeItem(tile.getTileWorkingMetadata().getConsumeSlot(), 1);
                }
            }
        } else {
            // We might be dealing with a bucket
            FluidActionResult filledContainer = FluidUtil.tryFillContainer(infuseStack, tile.getTank(), Integer.MAX_VALUE, null, true);
            if (filledContainer.isSuccess()) {
                ItemStack result = filledContainer.getResult();
                if (addToProduceSlot(tile, result)) {
                    tile.getInventory().removeItem(tile.getTileWorkingMetadata().getConsumeSlot(), 1);
                }
            }
        }
    }

    @Override
    public int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot) {
        return EmptyFluidContainerInTankTickAction.getRequiredTicks(tile, getInfuseStack(tile));
    }

    @Override
    public ItemStack willProduceItem(TileBloodInfuser tile) {
        ItemStack itemStack = tile.getInventory().getItem(tile.getTileWorkingMetadata().getConsumeSlot());
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (FluidUtil.getFluidHandler(itemStack).orElse(null) instanceof FluidContainerItemWrapperWithSimulation) {
            return ItemStack.EMPTY;
        }
        ItemStack smallContainer = FluidUtil.tryFillContainer(itemStack, tile.getTank(), MB_PER_TICK, null, false).getResult();
        if (!smallContainer.isEmpty()) {
            return smallContainer;
        }
        return FluidUtil.tryFillContainer(itemStack, tile.getTank(), FluidHelpers.BUCKET_VOLUME, null, false).getResult();
    }
    
}
