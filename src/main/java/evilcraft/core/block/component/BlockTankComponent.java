package evilcraft.core.block.component;

import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.item.DamageIndicatedItemComponent;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.item.BucketBlood;

/**
 * Component for block tanks.
 * @author rubensworks
 * @param <T> The type of block.
 *
 */
public class BlockTankComponent<T extends BlockContainer & IBlockTank> {
	
	private T tank;
	
	/**
	 * Make a new instance.
	 * @param tank
	 */
	public BlockTankComponent(T tank) {
		this.tank = tank;
	}

	/**
	 * Called upon tank activation.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param player
	 * @param side
	 * @param motionX
	 * @param motionY
	 * @param motionZ
	 * @return If the event should be halted.
	 */
	public boolean onBlockActivatedTank(World world, int x, int y, int z,
			EntityPlayer player, int side, float motionX, float motionY,
			float motionZ) {
		if(world.isRemote) {
            return false;
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(x, y, z);
            if(tile != null) {
                if(itemStack != null && itemStack.getItem() instanceof ItemBucket) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                    if(fluidStack != null &&
                            (tile.getTank().getAcceptedFluid() == null
                            || tile.getTank().canTankAccept(fluidStack.getFluid())
                            || tile.getTank().getFluidType() == null) && tile.getTank().canCompletelyFill(fluidStack)) {
                        tile.getTank().fill(fluidStack, true);
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }
                        return true;
                    } else if(itemStack.getItem() == Items.bucket && tile.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
                        tile.getTank().drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(BucketBlood.getInstance()));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
	}

	/**
     * Get info for a given itemStack.
     * @param itemStack The itemStack that must be given information.
     * @return Information for that itemStack.
     */
	public String getInfoTank(ItemStack itemStack) {
		int amount = 0;
        if(itemStack.getTagCompound() != null) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
            if(fluid != null)
                amount = fluid.amount;
        }
        return DamageIndicatedItemComponent.getInfo(amount, tank.getTankCapacity(itemStack));
	}

}
