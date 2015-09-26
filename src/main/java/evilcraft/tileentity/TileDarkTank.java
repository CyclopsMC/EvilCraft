package evilcraft.tileentity;

import evilcraft.GeneralConfig;
import evilcraft.block.DarkTank;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

/**
 * Tile Entity for the dark tank.
 * @author rubensworks
 *
 */
public class TileDarkTank extends TankInventoryTileEntity {
	
	/**
	 * The base capacity of the tank.
	 */
	public static final int BASE_CAPACITY = 16000;
	/**
	 * The NBT tag name of the tank.
	 */
	public static final String NBT_TAG_TANK = "darkTank";

	/**
	 * Make a new instance.
	 */
	public TileDarkTank() {
		super(0, "inventory", BASE_CAPACITY, NBT_TAG_TANK);
		this.setSendUpdateOnTankChanged(true);
	}
	
	@Override
	public boolean isItemValidForSlot(int side, ItemStack item) {
		return false;
	}

	@Override
    public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}
	
	/**
	 * Get the filled ratio of this tank.
	 * @return The ratio.
	 */
	public double getFillRatio() {
		return Math.min(1.0D, ((double) getTank().getFluidAmount()) / (double) getTank().getCapacity());
	}
	
	protected boolean shouldAutoDrain() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == DarkTank.META_DRAINING;
	}
	
	@Override
	protected void updateTileEntity() {
		if(!getTank().isEmpty() && shouldAutoDrain() && !getWorldObj().isRemote) {
			ForgeDirection down = ForgeDirection.DOWN;
			if(!worldObj.isAirBlock(xCoord + down.offsetX, yCoord + down.offsetY, zCoord + down.offsetZ)) {
				// Try to fill block below
				TileEntity tile = worldObj.getTileEntity(xCoord + down.offsetX, yCoord + down.offsetY, zCoord + down.offsetZ);
				if (tile instanceof IFluidHandler) {
					IFluidHandler handler = (IFluidHandler) tile;
					FluidStack fluidStack = new FluidStack(getTank().getFluidType(),
							Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
					if (handler.fill(down.getOpposite(), fluidStack, false) > 0) {
						int filled = handler.fill(down.getOpposite(), fluidStack, true);
						drain(filled, true);
					}
				}
			} else {
				// Try to fill fluid container items below
				List<EntityItem> items = worldObj.selectEntitiesWithinAABB(EntityItem.class,
						AxisAlignedBB.getBoundingBox(
								xCoord + down.offsetX, yCoord + down.offsetY, zCoord + down.offsetZ,
								xCoord + down.offsetX + 1, yCoord + down.offsetY + 1, zCoord + down.offsetZ + 1
						), IEntitySelector.selectAnything);
				for(EntityItem item : items) {
					if(item.getEntityItem() != null && item.getEntityItem().getItem() instanceof IFluidContainerItem &&
							item.getEntityItem().stackSize == 1) {
						ItemStack itemStack = item.getEntityItem().copy();
						IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
						FluidStack fluidStack = new FluidStack(getTank().getFluidType(),
								Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
						if(container.fill(item.getEntityItem(), fluidStack, false) > 0) {
							int filled = container.fill(itemStack, fluidStack, true);
							drain(filled, true);
							item.setEntityItemStack(itemStack);
						}
					}
				}
			}
		}
	}
	
	@Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
		return from != ForgeDirection.DOWN && super.canFill(from, fluid);
	}

}
