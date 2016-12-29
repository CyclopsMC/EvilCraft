package org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
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
        return super.canTick(tile, itemStack, slot, tick) && FluidUtil.getFluidHandler(itemStack) != null;
    }

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        ItemStack infuseStack = getInfuseStack(tile);
        if (!infuseStack.isEmpty()) {
            infuseStack = infuseStack.copy();
        }
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
            tile.getInventory().setInventorySlotContents(tile.getConsumeSlot(), infuseStack);
            if (FluidHelpers.getFluid(container) != null && FluidHelpers.getAmount(FluidHelpers.getFluid(container)) == FluidHelpers.getCapacity(container)) {
                if (addToProduceSlot(tile, infuseStack)) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                }
            }
        } else {
            // We might be dealing with a bucket
            ItemStack result = FluidUtil.tryFillContainer(infuseStack, tile.getTank(), Integer.MAX_VALUE, null, true).getResult();
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
        if (itemStack.isEmpty()) {
            return null;
        }
        if (FluidUtil.getFluidHandler(itemStack) instanceof FluidContainerItemWrapperWithSimulation) {
            return null;
        }
        ItemStack smallContainer = FluidUtil.tryFillContainer(itemStack, tile.getTank(), MB_PER_TICK, null, false).getResult();
        if (!smallContainer.isEmpty()) {
            return smallContainer;
        }
        return FluidUtil.tryFillContainer(itemStack, tile.getTank(), Fluid.BUCKET_VOLUME, null, false).getResult();
    }
    
}
