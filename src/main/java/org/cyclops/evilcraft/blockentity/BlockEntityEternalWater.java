package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Tile Entity for the eternal water blockState.
 * @author rubensworks
 *
 */
public class BlockEntityEternalWater extends CyclopsBlockEntity {

    public static final FluidStack WATER = new FluidStack(Fluids.WATER, Integer.MAX_VALUE);

    public BlockEntityEternalWater(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_ETERNAL_WATER.get(), blockPos, blockState);
    }

    public static class CapabilityRegistrar extends BlockEntityCapabilityRegistrar<BlockEntityEternalWater> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends BlockEntityEternalWater>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void populate() {
            add(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK, (blockEntity, direction) -> new InfiniteWaterFluidCapability());
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

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntityEternalWater> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityEternalWater blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            for(Direction direction : Direction.values()) {
                BlockEntityHelpers.getCapability(level, pos.relative(direction),
                                direction.getOpposite(), net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK)
                        .ifPresent(handler -> handler.fill(WATER, IFluidHandler.FluidAction.EXECUTE));
            }
        }
    }
}
