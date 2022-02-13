package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.ITickingTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.TickingTileComponent;

/**
 * Tile Entity for the eternal water blockState.
 * @author rubensworks
 *
 */
public class TileEternalWater extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    public static final FluidStack WATER = new FluidStack(Fluids.WATER, Integer.MAX_VALUE);

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    public TileEternalWater() {
        super(RegistryEntries.TILE_ENTITY_ETERNAL_WATER);
        addCapabilityInternal(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, LazyOptional.of(InfiniteWaterFluidCapability::new));
    }

	@Override
	protected void updateTileEntity() {
		if(!getLevel().isClientSide()) {
            for(Direction direction : Direction.values()) {
                TileHelpers.getCapability(getLevel(), getBlockPos().relative(direction),
                        direction.getOpposite(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                        .ifPresent(handler -> handler.fill(WATER, IFluidHandler.FluidAction.EXECUTE));
            }
		}
	}

    public static class InfiniteWaterFluidCapability implements IFluidHandler {
        @Override
        public int getTanks() {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return WATER;
        }

        @Override
        public int getTankCapacity(int tank) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || resource.getFluid() != WATER.getFluid()) {
                return FluidStack.EMPTY;
            }
            return new FluidStack(WATER.getFluid(), resource.getAmount());
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return new FluidStack(WATER.getFluid(), maxDrain);
        }
    }
}
