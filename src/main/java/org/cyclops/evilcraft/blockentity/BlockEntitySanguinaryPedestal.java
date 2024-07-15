package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.fluid.FluidHandlerWrapper;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;
import org.cyclops.evilcraft.block.BlockSanguinaryPedestalConfig;
import org.cyclops.evilcraft.core.algorithm.RegionIterator;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;
import org.cyclops.evilcraft.network.packet.SanguinaryPedestalBlockReplacePacket;

import java.util.function.Supplier;

/**
 * Tile for the {@link org.cyclops.evilcraft.block.BlockSanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class BlockEntitySanguinaryPedestal extends BlockEntityTankInventory {

    private static final int MB_RATE = 100;
    public static final int TANK_BUCKETS = 10;
    private static final int OFFSET = 2;
    private static final int OFFSET_EFFICIENCY = 4;
    private static final int ACTIONS_PER_TICK_EFFICIENCY = 5;

    private final IFluidHandler bonusFluidHandler;

    private RegionIterator regionIterator;

    public BlockEntitySanguinaryPedestal(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_SANGUINARY_PEDESTAL.get(), blockPos, blockState, 0, 1, FluidHelpers.BUCKET_VOLUME * TANK_BUCKETS, RegistryEntries.FLUID_BLOOD.get());
        this.bonusFluidHandler = new FluidHandlerWrapper(getTank()) {
            @Override
            public int fill(FluidStack resource, FluidAction action) {
                if (hasEfficiency() && !resource.isEmpty()) {
                    resource.setAmount((int) (resource.getAmount() * BlockSanguinaryPedestalConfig.efficiencyBoost));
                }
                return super.fill(resource, action);
            }
        };
    }

    public static class CapabilityRegistrar extends BlockEntityTankInventory.CapabilityRegistrar<BlockEntitySanguinaryPedestal> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends BlockEntitySanguinaryPedestal>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void registerTankInventoryCapabilitiesItem() {
            // Don't expose inventory
        }
    }

    /**
     * @return The inner tank of this pedestal that when filled can have a bonus applied.
     */
    public IFluidHandler getBonusFluidHandler() {
        return bonusFluidHandler;
    }

    protected void afterBlockReplace(ServerLevel world, BlockPos location) {
        // NOTE: this is only called server-side, so make sure to send packets where needed.
        EvilCraft._instance.getPacketHandler().sendToAllAround(new SanguinaryPedestalBlockReplacePacket(location.getX(), location.getY(), location.getZ()),
                LocationHelpers.createTargetPointFromLocation(world, location, SanguinaryPedestalBlockReplacePacket.RANGE));
    }

    protected boolean hasEfficiency() {
        return ((org.cyclops.evilcraft.block.BlockSanguinaryPedestal) getBlockState().getBlock()).getTier() == 1;
    }

    private BlockPos getNextLocation() {
        if(regionIterator == null) {
            regionIterator = new RegionIterator(getBlockPos(), (hasEfficiency() ? OFFSET_EFFICIENCY : OFFSET), true);
        }
        return regionIterator.next();
    }

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntitySanguinaryPedestal> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntitySanguinaryPedestal blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            int actions = blockEntity.hasEfficiency() ? ACTIONS_PER_TICK_EFFICIENCY : 1;
            // Drain next blockState in tick
            while(!blockEntity.getTank().isFull() && actions > 0) {
                BlockPos location = blockEntity.getNextLocation();
                Block block = level.getBlockState(location).getBlock();
                if(block instanceof BlockBloodStain) {
                    BlockEntityHelpers.getCapability(level, location, net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK)
                            .ifPresent((source) -> {
                                FluidStack moved = FluidUtil.tryFluidTransfer(blockEntity.getBonusFluidHandler(), source, Integer.MAX_VALUE, true);
                                if (!moved.isEmpty()) {
                                    blockEntity.afterBlockReplace((ServerLevel) level, location);
                                }
                            });
                }
                actions--;
            }

            // Auto-drain the inner tank
            if(!blockEntity.getTank().isEmpty()) {
                for(Direction direction : Direction.values()) {
                    BlockEntityHelpers.getCapability(level, pos.relative(direction),
                                    direction.getOpposite(), net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK)
                            .ifPresent(handler -> {
                                if(!blockEntity.getTank().isEmpty()) {
                                    FluidStack fluidStack = new FluidStack(blockEntity.getTank().getFluid().getFluid(), Math.min(MB_RATE, blockEntity.getTank().getFluidAmount()));
                                    if(handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
                                        int filled = handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                                        blockEntity.getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
                                    }
                                }
                            });
                }
            }
        }
    }
}
