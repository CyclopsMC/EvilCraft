package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.api.fluids.SingleUseTank;
import evilcraft.entities.tileentities.IConsumeProduceEmptyInTankTile;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.items.BucketBlood;

public class EmptyFluidContainerInTankTickAction extends EmptyInTankTickAction {

    @Override
    public void onTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        ItemStack containerStack = getEmptyStack(tile);
        ItemFluidContainer container = (ItemFluidContainer) containerStack.getItem();
        if(container.getFluid(containerStack) != null) {
            FluidStack fluidStack = container.getFluid(containerStack).copy();
            fluidStack.amount = MB_PER_TICK;
            int filled = tile.getTank().fill(fluidStack, true);
            container.drain(containerStack, filled, true);
        }
    }
    
    @Override
    public int getRequiredTicks(IConsumeProduceEmptyInTankTile tile) {
        return FluidContainerItemTickAction.getRequiredTicks(tile, getEmptyStack(tile));
    }
    
    @Override
    public boolean canTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        boolean emptyContainer = false;
        ItemStack containerStack = getEmptyStack(tile);
        ItemFluidContainer container = (ItemFluidContainer) containerStack.getItem();
        if(container.getFluid(containerStack) != null) {
            FluidStack fluidStack = container.getFluid(containerStack);
            if(fluidStack.amount <= 0)
                emptyContainer = true;
        } else emptyContainer = true;
        return super.canTick(tile, tick) && !emptyContainer;
    }

}
