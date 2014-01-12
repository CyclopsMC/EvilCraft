package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.items.BucketBlood;

public class ItemBucketTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceWithTankTile tile, int tick) {
        if(tick >= getRequiredTicks(tile)) {
            ItemStack infuseStack = getInfuseStack(tile);
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(infuseStack);
            if(tile.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME && fluidStack == null) {
                tile.getTank().drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                tile.getInventory().setInventorySlotContents(tile.getProduceSlot(), new ItemStack(BucketBlood.getInstance()));
            }
        }
    }
    
    private int getRequiredTicks(IConsumeProduceWithTankTile tile) {
        return 10;
    }

}
