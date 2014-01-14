package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.entities.tileentities.IConsumeProduceEmptyInTankTile;

public class FluidContainerItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        ItemStack infuseStack = getInfuseStack(tile);
        ItemFluidContainer container = (ItemFluidContainer) infuseStack.getItem();
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
    public int getRequiredTicks(IConsumeProduceEmptyInTankTile tile) {
        return getRequiredTicks(tile, getInfuseStack(tile));
    }
    
    public static int getRequiredTicks(IConsumeProduceEmptyInTankTile tile, ItemStack itemStack) {
        ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
        int amount = 0;
        if(container.getFluid(itemStack) != null)
            amount = container.getFluid(itemStack).amount;
        int capacity = Math.min(container.getCapacity(itemStack), tile.getTank().getFluidAmount());
        return (capacity - amount) / MB_PER_TICK;
    }

    @Override
    public int willProduceItemID(IConsumeProduceEmptyInTankTile tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot()).itemID;
    }
    
}
