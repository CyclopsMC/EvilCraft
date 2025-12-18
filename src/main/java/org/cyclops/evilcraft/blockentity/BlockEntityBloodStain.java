package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;

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
            add(net.neoforged.neoforge.capabilities.Capabilities.Fluid.BLOCK, (blockEntity, direction) -> new FluidHandler(blockEntity));
        }
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    public void setAmount(Integer amount, boolean committedChange) {
        this.amount = amount;
        if (this.amount == 0 && committedChange) {
            getLevel().removeBlock(getBlockPos(), false);
        }
        if (committedChange) {
            setChanged();
        }
    }

    public void addAmount(int amount, boolean committedChange) {
        setAmount(Math.min(CAPACITY, Math.max(0, this.amount + amount)), committedChange);
    }

    public static class FluidHandler implements ResourceHandler<FluidResource> {
        private final BlockEntityBloodStain tile;
        private final Journal journal;

        public FluidHandler(BlockEntityBloodStain tile) {
            this.tile = tile;
            this.journal = new Journal();
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public FluidResource getResource(int tank) {
            return FluidResource.of(RegistryEntries.FLUID_BLOOD);
        }

        @Override
        public long getAmountAsLong(int tank) {
            return tile.getAmount();
        }

        @Override
        public long getCapacityAsLong(int tank, FluidResource resource) {
            return CAPACITY;
        }

        @Override
        public boolean isValid(int tank, FluidResource resource) {
            return tank == 0 && resource.getFluid() == RegistryEntries.FLUID_BLOOD.get();
        }

        @Override
        public int insert(int tank, FluidResource resource, int amount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public int extract(int tank, FluidResource resource, int amount, TransactionContext transaction) {
            this.journal.updateSnapshots(transaction);
            amount = Math.min(tile.getAmount(), amount);
            tile.addAmount(-amount, false);
            return amount;
        }

        public class Journal extends SnapshotJournal<Integer> {
            @Override
            protected Integer createSnapshot() {
                return tile.getAmount();
            }

            @Override
            protected void revertToSnapshot(Integer integer) {
                tile.setAmount(integer, false);
            }

            @Override
            protected void onRootCommit(Integer originalState) {
                super.onRootCommit(originalState);
                tile.setAmount(originalState, true);
            }
        }
    }

}
