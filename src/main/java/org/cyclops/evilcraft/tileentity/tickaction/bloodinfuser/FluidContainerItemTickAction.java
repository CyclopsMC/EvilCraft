package org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.apache.commons.lang3.mutable.MutableInt;
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
        IFluidContainerItem container = (IFluidContainerItem) infuseStack.getItem();
        FluidStack fluidStack = tile.getTank().getFluid().copy();

        MutableInt duration = new MutableInt(MB_PER_TICK);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_FILLBLOODPERTICK));
        int minAmount = duration.getValue();

        fluidStack.amount = Math.min(minAmount, fluidStack.amount);
        int filled = container.fill(infuseStack, fluidStack, true);
        tile.getTank().drain(filled, true);
        if(container.getFluid(infuseStack) != null && container.getFluid(infuseStack).amount == container.getCapacity(infuseStack)) {
            if(addToProduceSlot(tile, infuseStack)) {
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
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
}
