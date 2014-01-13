package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.items.BucketBlood;

public class FluidContainerItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceWithTankTile tile, int tick) {
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
    public int getRequiredTicks(IConsumeProduceWithTankTile tile) {
        ItemStack infuseStack = getInfuseStack(tile);
        ItemFluidContainer container = (ItemFluidContainer) infuseStack.getItem();
        int amount = 0;
        if(container.getFluid(infuseStack) != null)
            amount = container.getFluid(infuseStack).amount;
        int capacity = Math.min(container.getCapacity(infuseStack), tile.getTank().getFluidAmount());
        return (capacity - amount) / MB_PER_TICK;
    }

    @Override
    public int willProduceItemID(IConsumeProduceWithTankTile tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot()).itemID;
    }
    
}
