package evilcraft.tileentity.tickaction.bloodinfuser;

import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.item.BucketBloodConfig;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * {@link ITickAction} that can fill empty buckets with blood.
 * @author rubensworks
 *
 */
public class ItemBucketTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot)) {
            ItemStack infuseStack = getInfuseStack(tile);
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(infuseStack);
            if(tile.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME && fluidStack == null) {
                if(addToProduceSlot(tile, new ItemStack(BucketBloodConfig._instance.getItemInstance()))) {
                    tile.getTank().drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                }
            }
        }
    }
    
    @Override
    public int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot) {
        return FluidContainerRegistry.BUCKET_VOLUME / MB_PER_TICK;
    }

    @Override
    public ItemStack willProduceItem(TileBloodInfuser tile) {
        return new ItemStack(BucketBloodConfig._instance.getItemInstance());
    }

}
