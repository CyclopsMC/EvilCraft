package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;

import java.util.function.Supplier;

/**
 * Tile Entity for the entangled chalice.
 * @author rubensworks
 *
 */
public class BlockEntityEntangledChalice extends CyclopsBlockEntity {

    /**
     * The base capacity of the tank.
     */
    public static final int BASE_CAPACITY = 4000;

    private final WorldSharedTank tank;

    public BlockEntityEntangledChalice(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_ENTANGLED_CHALICE.get(), blockPos, blockState);
        tank = new WorldSharedTank(BASE_CAPACITY);
    }

    public static class CapabilityRegistrar extends BlockEntityCapabilityRegistrar<BlockEntityEntangledChalice> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends BlockEntityEntangledChalice>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void populate() {
            add(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK, (blockEntity, direction) -> blockEntity.getTank());
        }
    }

    public WorldSharedTank getTank() {
        return tank;
    }

    /**
     * Get the filled ratio of this tank.
     * @return The ratio.
     */
    public double getFillRatio() {
        int prev = ((WorldSharedTank) getTank()).getPreviousAmount();
        float alpha = (float) (WorldSharedTankCache.getInstance().getTickOffset()) / (float) WorldSharedTankCache.INTERPOLATION_TICK_OFFSET;
        double interpolatedAmount = prev * (1.0F - alpha) + getTank().getFluidAmount() * alpha;
        return Math.min(1.0D, (interpolatedAmount) / (double) getTank().getCapacity());
    }

    /**
     * @return The unique key of the internal tank.
     */
    public String getWorldTankId() {
        return ((WorldSharedTank) getTank()).getTankID();
    }

    /**
     * Set the unique key of the internal tank.
     * @param tankId The new id.
     */
    public void setWorldTankId(String tankId) {
        ((WorldSharedTank) getTank()).setTankID(tankId);
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        super.read(tag, holderLookupProvider);
        tank.readFromNBT(holderLookupProvider, tag, "tank");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        tank.writeToNBT(holderLookupProvider, tag, "tank");
        super.saveAdditional(tag, holderLookupProvider);
    }

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntityEntangledChalice> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityEntangledChalice blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            blockEntity.getTank().resetPreviousFluid(); // Optimization for map look-ups in the shared tank.
        }
    }

}
