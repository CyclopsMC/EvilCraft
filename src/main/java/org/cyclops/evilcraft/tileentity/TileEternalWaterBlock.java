package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import javax.annotation.Nullable;

/**
 * Tile Entity for the eternal water blockState.
 * @author rubensworks
 *
 */
public class TileEternalWaterBlock extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    public static final FluidStack WATER = new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE);

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    public TileEternalWaterBlock() {
        addCapabilityInternal(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, new InfiniteWaterFluidCapability());
    }

	@Override
	protected void updateTileEntity() {
		if(!getWorld().isRemote) {
            for(EnumFacing direction : EnumFacing.VALUES) {
                IFluidHandler handler = TileHelpers.getCapability(getWorld(), getPos().offset(direction),
                        direction.getOpposite(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                if (handler != null) {
                    handler.fill(WATER, true);
                }
            }
		}
	}

    public static class InfiniteWaterFluidCapability implements IFluidHandler {
        @Override
        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[] { new FluidTankProperties(WATER, Integer.MAX_VALUE) };
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (resource == null || resource.getFluid() != WATER.getFluid()) {
                return null;
            }
            return new FluidStack(WATER.getFluid(), resource.amount);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return new FluidStack(WATER.getFluid(), maxDrain);
        }
    }
}
