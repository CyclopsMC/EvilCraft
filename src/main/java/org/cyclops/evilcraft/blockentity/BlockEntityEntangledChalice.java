package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;

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
        super(RegistryEntries.BLOCK_ENTITY_ENTANGLED_CHALICE, blockPos, blockState);
        tank = new WorldSharedTank(BASE_CAPACITY);
        addCapabilityInternal(ForgeCapabilities.FLUID_HANDLER, LazyOptional.of(this::getTank));
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
    public void read(CompoundTag tag) {
        super.read(tag);
        tank.readFromNBT(tag, "tank");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tank.writeToNBT(tag, "tank");
        super.saveAdditional(tag);
    }

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntityEntangledChalice> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityEntangledChalice blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            blockEntity.getTank().resetPreviousFluid(); // Optimization for map look-ups in the shared tank.
        }
    }

}
