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
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
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
            	FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                if(fluidStack != null) { // Fill the tank.
                	if(canTankBeFilled(tile, fluidStack) && tile.getTank().canCompletelyFill(fluidStack)) {
	                    tile.fill(fluidStack, true);
	                    if(!player.capabilities.isCreativeMode) {
	                    	ItemStack drainedItem = FluidContainerRegistry.drainFluidContainer(itemStack);
                            if(drainedItem.stackSize == 0) drainedItem = null;
	                    	InventoryHelpers.tryReAddToStack(player, itemStack, drainedItem);
	                    }
                	}
                    return true;
                } else if(tile.getTank().getFluidAmount() > 0) { // Drain the tank.
                	ItemStack filledItem = FluidContainerRegistry.fillFluidContainer(tile.getTank().getFluid(), itemStack);
                	FluidStack filledAmount = FluidContainerRegistry.getFluidForFilledItem(filledItem);
                	if(filledAmount != null) {
                		tile.drain(filledAmount, true);
                        if(!player.capabilities.isCreativeMode) {
                        	InventoryHelpers.tryReAddToStack(player, itemStack, filledItem);
                        }
                        return true;
                	}
                }
                if(itemStack.getItem() instanceof IFluidContainerItem) {
                    IFluidContainerItem containerItem = ((IFluidContainerItem) itemStack.getItem());
                    fluidStack = containerItem.getFluid(itemStack);
                    if(player.isSneaking()) { // Drain from tank into item.
                        if(tile.getTank().getFluidAmount() > 0) {
                            FluidStack fluidStackTemp = new FluidStack(tile.getTank().getFluid().getFluid(),
                                    FluidContainerRegistry.BUCKET_VOLUME);
                            if(containerItem.fill(itemStack, fluidStackTemp, false) > 0) {
                                tile.getTank().drain(containerItem.fill(itemStack, fluidStackTemp, true), true);
                            }
                        }

                    } else if(fluidStack != null) { // Fill to tank from item.
                        FluidStack fluidStackTemp = new FluidStack(fluidStack.getFluid(),
                                Math.min(FluidContainerRegistry.BUCKET_VOLUME, fluidStack.amount));
                        if(canTankBeFilled(tile, fluidStack) && tile.getTank().fill(fluidStackTemp, false) > 0) {
                            containerItem.drain(itemStack, tile.fill(fluidStackTemp, true), true);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
	}

    protected static boolean canTankBeFilled(TankInventoryTileEntity tile, FluidStack fluidStack) {
        return tile.getTank().getAcceptedFluid() == null
                || fluidStack == null
                || tile.getTank().getFluidType() == null
                || tile.getTank().canTankAccept(fluidStack.getFluid());
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
