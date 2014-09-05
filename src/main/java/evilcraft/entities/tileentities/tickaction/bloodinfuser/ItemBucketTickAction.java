package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.core.entities.tileentitites.tickaction.ITickAction;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.items.BucketBlood;

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
                if(addToProduceSlot(tile, new ItemStack(BucketBlood.getInstance()))) {
                    tile.getTank().drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                }
            }
        }
    }
    
    @Override
    public int getRequiredTicks(TileBloodInfuser tile, int slot) {
        return FluidContainerRegistry.BUCKET_VOLUME / MB_PER_TICK;
    }

    @Override
    public Item willProduceItem(TileBloodInfuser tile) {
        return BucketBlood.getInstance();
    }

}
