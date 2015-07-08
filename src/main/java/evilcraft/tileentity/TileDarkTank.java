package evilcraft.tileentity;

import evilcraft.GeneralConfig;
import evilcraft.block.DarkTank;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

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
	public boolean isItemValidForSlot(int index, ItemStack item) {
		return false;
	}

	@Override
    public int[] getSlotsForFace(EnumFacing side) {
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
		return (Boolean) worldObj.getBlockState(getPos()).getValue(DarkTank.DRAINING);
	}
	
	@Override
	protected void updateTileEntity() {
		if(!getTank().isEmpty() && shouldAutoDrain() && !getWorld().isRemote) {
			EnumFacing down = EnumFacing.DOWN;
            TileEntity tile = worldObj.getTileEntity(getPos().offset(down));
			if(tile instanceof IFluidHandler) {
				IFluidHandler handler = (IFluidHandler) tile;
				FluidStack fluidStack = new FluidStack(getTank().getFluidType(),
                        Math.min(GeneralConfig.mbFlowRate, getTank().getFluidAmount()));
				if(handler.fill(down.getOpposite(), fluidStack, false) > 0) {
					int filled = handler.fill(down.getOpposite(), fluidStack, true);
					drain(filled, true);
				}
			}
		}
	}
	
	@Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
		return from != EnumFacing.DOWN && super.canFill(from, fluid);
	}

}
