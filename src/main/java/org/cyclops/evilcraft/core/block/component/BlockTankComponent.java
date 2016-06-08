package org.cyclops.evilcraft.core.block.component;

import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.item.DamageIndicatedItemComponent;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.block.IBlockTank;

/**
 * Component for blockState tanks.
 * @author rubensworks
 * @param <T> The type of blockState.
 *
 */
public class BlockTankComponent<T extends BlockContainer & IBlockTank> {
	
	private T tank;
	
	/**
	 * Make a new instance.
	 * @param tank The tank.
	 */
	public BlockTankComponent(T tank) {
		this.tank = tank;
	}

	/**
	 * Called upon tank activation.
	 * @param world The world.
	 * @param blockPos The position.
	 * @param player Player
     * @param hand The hand
     * @param itemStack The held item
	 * @param side Side integer
	 * @param motionX X motion
	 * @param motionY Y motion
	 * @param motionZ Z motion
	 * @return If the event should be halted.
	 */
	public boolean onBlockActivatedTank(World world, BlockPos blockPos,
			EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float motionX, float motionY,
			float motionZ) {
        TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(blockPos);
        if(tile != null) {
            if(itemStack != null) {
                SingleUseTank tank = tile.getTank();
            	IFluidHandler itemFluidHandler = FluidUtil.getFluidHandler(itemStack);
                if(!player.isSneaking() && !tank.isFull() && itemFluidHandler != null) { // Fill the tank.
                	if(FluidUtil.tryEmptyContainer(itemStack, tank, Integer.MAX_VALUE, player, false) != null
                            && FluidHelpers.canCompletelyFill(itemFluidHandler, tank)) {
                        ItemStack drainedItem = FluidUtil.tryEmptyContainer(itemStack, tank, Integer.MAX_VALUE, player, true);
	                    if(!player.capabilities.isCreativeMode) {
                            if(drainedItem != null && drainedItem.stackSize == 0) drainedItem = null;
	                    	InventoryHelpers.tryReAddToStack(player, itemStack, drainedItem);
	                    }
                	}
                    return true;
                } else if(!tank.isEmpty()
                        && FluidUtil.tryFillContainer(itemStack, tank, Integer.MAX_VALUE, player, false) != null) { // Drain the tank.
                    ItemStack filledItem = FluidUtil.tryFillContainer(itemStack, tank, Integer.MAX_VALUE, player, true);
                	if(!player.capabilities.isCreativeMode) {
                        InventoryHelpers.tryReAddToStack(player, itemStack, filledItem);
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
		FluidStack fluidStack = null;
		if(itemStack.getItem() instanceof IFluidContainerItem) {
			fluidStack = ((IFluidContainerItem) itemStack.getItem()).getFluid(itemStack);
		} else if(itemStack.getTagCompound() != null) {
            fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
        }
		if(fluidStack != null) {
            amount = fluidStack.amount;
		}
        return DamageIndicatedItemComponent.getInfo(fluidStack, amount, tank.getTankCapacity(itemStack));
	}
	
	/**
     * Write additional info about the tile into the item.
     * @param tile The tile that is being broken.
     * @param tag The tag that will be added to the dropped item.
     */
    public void writeAdditionalInfo(TileEntity tile, NBTTagCompound tag) {
    	if(tile instanceof TankInventoryTileEntity) {
			TankInventoryTileEntity tankTile = (TankInventoryTileEntity) tile;
			tank.setTankCapacity(tag, tankTile.getTank().getCapacity());
		}
    }

}
