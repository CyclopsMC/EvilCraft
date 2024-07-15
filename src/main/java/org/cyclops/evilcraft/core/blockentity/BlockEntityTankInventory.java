package org.cyclops.evilcraft.core.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class BlockEntityTankInventory extends CyclopsBlockEntity {

    private final SimpleInventory inventory;
    private final SingleUseTank tank;
    protected final int tankSize;

    public BlockEntityTankInventory(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState, int inventorySize, int stackSize, int tankSize, @Nullable Fluid acceptedFluid) {
        super(type, blockPos, blockState);
        inventory = createInventory(inventorySize, stackSize);
        tank = createTank(tankSize);
        this.tankSize = tankSize;
        if (acceptedFluid != null) {
            tank.setAcceptedFluid(acceptedFluid);
        }

        // Add update listeners
        inventory.addDirtyMarkListener(this);
        tank.addDirtyMarkListener(this::onTankChanged);
    }

    public static class CapabilityRegistrar<T extends BlockEntityTankInventory> extends BlockEntityCapabilityRegistrar<T> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends T>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void populate() {
            registerTankInventoryCapabilitiesItem();
            registerTankInventoryCapabilitiesFluid();
        }

        public void registerTankInventoryCapabilitiesItem() {
            add(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK, (blockEntity, direction) -> blockEntity.getInventory().getItemHandlerSided(direction));
        }

        public void registerTankInventoryCapabilitiesFluid() {
            add(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK, (blockEntity, direction) -> blockEntity.getTank());
        }
    }

    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize);
    }

    protected SingleUseTank createTank(int tankSize) {
        return new SingleUseTank(tankSize);
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        super.read(tag, holderLookupProvider);
        inventory.readFromNBT(holderLookupProvider, tag, "inventory");
        tank.readFromNBT(holderLookupProvider, tag, "tank");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider holderLookupProvider) {
        inventory.writeToNBT(holderLookupProvider, tag, "inventory");
        tank.writeToNBT(holderLookupProvider, tag, "tank");
        super.saveAdditional(tag, holderLookupProvider);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public SingleUseTank getTank() {
        return tank;
    }

    public void onTankChanged() {
        setChanged();
        getInventory().setChanged();
    }
}
