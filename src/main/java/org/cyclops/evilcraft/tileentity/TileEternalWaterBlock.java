package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * Tile Entity for the eternal water blockState.
 * @author rubensworks
 *
 */
public class TileEternalWaterBlock extends CyclopsTileEntity implements IFluidHandler, CyclopsTileEntity.ITickingTile {

    public static final FluidStack WATER = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

	@Override
	protected void updateTileEntity() {
		if(!getWorld().isRemote) {
            for(EnumFacing direction : EnumFacing.VALUES) {
                TileEntity tile = worldObj.getTileEntity(getPos().offset(direction));
                if (tile instanceof IFluidHandler) {
                    IFluidHandler handler = (IFluidHandler) tile;
                    FluidStack fluidStack = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
                    handler.fill(direction.getOpposite(), fluidStack, true);
                }
            }
		}
	}

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if(resource == null || resource.getFluid() != FluidRegistry.WATER) return null;
        return drain(from, FluidContainerRegistry.BUCKET_VOLUME, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return new FluidStack(FluidRegistry.WATER, maxDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] {
            new FluidTankInfo(WATER.copy(), FluidContainerRegistry.BUCKET_VOLUME)
        };
    }
}
