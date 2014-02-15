package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.entities.tileentities.tickaction.EmptyFluidContainerInTankTickAction;

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
        fluidStack.amount = Math.min(MB_PER_TICK, fluidStack.amount);
        int filled = container.fill(infuseStack, fluidStack, true);
        tile.getTank().drain(filled, true);
        if(container.getFluid(infuseStack).amount == container.getCapacity(infuseStack)) {
            if(addToProduceSlot(tile, infuseStack)) {
                tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
            }
        }
    }

    @Override
    public int getRequiredTicks(TileBloodInfuser tile, int slot) {
        return EmptyFluidContainerInTankTickAction.getRequiredTicks(tile, getInfuseStack(tile));
    }

    @Override
    public Item willProduceItem(TileBloodInfuser tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot()).getItem();
    }
    
}
