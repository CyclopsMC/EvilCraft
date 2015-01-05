package evilcraft.tileentity;

import evilcraft.core.tileentity.EvilCraftTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Tile Entity for the eternal water block.
 * @author rubensworks
 *
 */
public class TileEternalWaterBlock extends EvilCraftTileEntity implements IFluidHandler {

    public static final FluidStack WATER = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);

	@Override
	protected void updateTileEntity() {
		if(!getWorldObj().isRemote) {
            for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY,
                        zCoord + direction.offsetZ);
                if (tile instanceof IFluidHandler) {
                    IFluidHandler handler = (IFluidHandler) tile;
                    FluidStack fluidStack = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
                    handler.fill(direction.getOpposite(), fluidStack, true);
                }
            }
		}
	}

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if(resource == null || resource.getFluid() != FluidRegistry.WATER) return null;
        return drain(from, FluidContainerRegistry.BUCKET_VOLUME, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return new FluidStack(FluidRegistry.WATER, maxDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] {
            new FluidTankInfo(WATER.copy(), FluidContainerRegistry.BUCKET_VOLUME)
        };
    }
}
