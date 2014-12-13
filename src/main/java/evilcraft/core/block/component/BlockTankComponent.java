package evilcraft.core.block.component;

import evilcraft.core.block.IBlockTank;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.item.DamageIndicatedItemComponent;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

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
        ItemStack itemStack = player.inventory.getCurrentItem();
        TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(x, y, z);
        if(tile != null) {
            if(itemStack != null) {
            	FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                if(fluidStack != null) { // Fill the tank.
                	if(canTankBeFilled(tile, fluidStack) && tile.getTank().canCompletelyFill(fluidStack)) {
	                    tile.fill(fluidStack, true);
	                    if(!player.capabilities.isCreativeMode) {
	                    	ItemStack drainedItem = FluidContainerRegistry.drainFluidContainer(itemStack);
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
                            if(containerItem.fill(itemStack, tile.getTank().getFluid(), false) > 0) {
                                tile.getTank().drain(containerItem.fill(itemStack,
                                        tile.getTank().getFluid(), true), true);
                            }
                        }

                    } else { // Fill to tank from item.
                        if(canTankBeFilled(tile, fluidStack) && tile.getTank().fill(fluidStack, false) > 0) {
                            containerItem.drain(itemStack, tile.fill(fluidStack, true), true);
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
