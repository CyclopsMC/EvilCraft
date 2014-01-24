package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.entities.tileentities.tickaction.EmptyFluidContainerInTankTickAction;

public class FluidContainerItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        ItemStack infuseStack = getInfuseStack(tile);
        IFluidContainerItem container = (IFluidContainerItem) infuseStack.getItem();
        FluidStack fluid = tile.getTank().getFluid().copy();
        fluid.amount = MB_PER_TICK;
        int filled = container.fill(infuseStack, fluid, true);
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
    public int willProduceItemID(TileBloodInfuser tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot()).itemID;
    }
    
}
