package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.entities.tileentities.IConsumeProduceEmptyInTankTile;
import evilcraft.items.BucketBlood;
import evilcraft.items.BucketBloodConfig;

public class ItemBucketTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        if(tick >= getRequiredTicks(tile)) {
            ItemStack infuseStack = getInfuseStack(tile);
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(infuseStack);
            if(tile.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME && fluidStack == null) {
                if(addToProduceSlot(tile, new ItemStack(BucketBlood.getInstance()))) {
                    tile.getTank().drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                }
            }
        }
    }
    
    @Override
    public int getRequiredTicks(IConsumeProduceEmptyInTankTile tile) {
        return FluidContainerRegistry.BUCKET_VOLUME / MB_PER_TICK;
    }

    @Override
    public int willProduceItemID(IConsumeProduceEmptyInTankTile tile) {
        return BucketBloodConfig._instance.ID;
    }

}
