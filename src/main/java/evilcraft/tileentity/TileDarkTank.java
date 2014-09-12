package evilcraft.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import evilcraft.block.DarkTank;
import evilcraft.core.tileentity.TankInventoryTileEntity;

/**
 * Tile Entity for the dark tank.
 * @author rubensworks
 *
 */
public class TileDarkTank extends TankInventoryTileEntity {
	
	private static final int MB_RATE = 10;
	
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
		if(!getTank().isEmpty() && shouldAutoDrain()) {
			ForgeDirection down = ForgeDirection.DOWN;
			TileEntity tile = worldObj.getTileEntity(xCoord + down.offsetX, yCoord + down.offsetY, zCoord + down.offsetZ);
			if(tile instanceof IFluidHandler) {
				IFluidHandler handler = (IFluidHandler) tile;
				FluidStack fluidStack = new FluidStack(getTank().getFluidType(), MB_RATE);
				if(handler.canFill(down.getOpposite(), getTank().getFluidType())
						&& handler.fill(down.getOpposite(), fluidStack, false) > 0) {
					int filled = handler.fill(down.getOpposite(), fluidStack, true);
					drain(filled, true);
				}
			}
		}
	}
	
	@Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(from == ForgeDirection.DOWN) {
			return false;
		}
		return super.canFill(from, fluid);
	}

}
