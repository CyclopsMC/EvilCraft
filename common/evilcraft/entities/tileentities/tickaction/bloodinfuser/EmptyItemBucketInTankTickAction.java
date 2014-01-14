package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.api.fluids.SingleUseTank;
import evilcraft.entities.tileentities.IConsumeProduceEmptyInTankTile;
import evilcraft.items.BucketBlood;

public class EmptyItemBucketInTankTickAction extends EmptyInTankTickAction {

    @Override
    public void onTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        if(tick >= getRequiredTicks(tile)) {
            ItemStack infuseStack = getEmptyStack(tile);
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(infuseStack);
            SingleUseTank tank = tile.getTank();
            if(tank.getCapacity() - tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME
                    && fluidStack != null && tank.getAcceptedFluid().equals(fluidStack.getFluid())) {
                tank.fill(fluidStack, true);
                tile.getInventory().setInventorySlotContents(tile.getEmptyToTankSlot(), FluidContainerRegistry.EMPTY_BUCKET.copy());
            }
        }
    }
    
    @Override
    public int getRequiredTicks(IConsumeProduceEmptyInTankTile tile) {
        return FluidContainerRegistry.BUCKET_VOLUME / MB_PER_TICK;
    }

}
