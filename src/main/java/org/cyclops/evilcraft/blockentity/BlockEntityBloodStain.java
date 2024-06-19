package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Tile for the {@link org.cyclops.evilcraft.block.BlockBloodStain}.
 * @author rubensworks
 *
 */
public class BlockEntityBloodStain extends CyclopsBlockEntity {

    public static final int CAPACITY = 5000;

    @NBTPersist
    private Integer amount = 0;

    public BlockEntityBloodStain(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_BLOOD_STAIN.get(), blockPos, blockState);
    }

    public static class CapabilityRegistrar extends BlockEntityCapabilityRegistrar<BlockEntityBloodStain> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends BlockEntityBloodStain>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void populate() {
            add(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK, (blockEntity, direction) -> new FluidHandler(blockEntity));
        }
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to add
     */
    public void addAmount(int amount) {
        this.amount = Math.min(CAPACITY, Math.max(0, this.amount + amount));
        if (this.amount == 0) {
            getLevel().removeBlock(getBlockPos(), false);
        }
        setChanged();
    }

    public static class FluidHandler implements IFluidHandler {
        private final BlockEntityBloodStain tile;

        public FluidHandler(BlockEntityBloodStain tile) {
            this.tile = tile;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return new FluidStack(RegistryEntries.FLUID_BLOOD, tile.getAmount());
        }

        @Override
        public int getTankCapacity(int tank) {
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return tank == 0 && stack.getFluid() == RegistryEntries.FLUID_BLOOD.get();
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.getFluid() == RegistryEntries.FLUID_BLOOD.get()) {
                return drain(resource.getAmount(), action);
            }
            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            maxDrain = Math.min(tile.getAmount(), maxDrain);
            FluidStack drained = new FluidStack(RegistryEntries.FLUID_BLOOD, maxDrain);
            if (action.execute()) {
                tile.addAmount(-maxDrain);
            }
            return drained;
        }
    }

}
