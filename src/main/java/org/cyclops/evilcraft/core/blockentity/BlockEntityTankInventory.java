package org.cyclops.evilcraft.core.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.capability.registrar.BlockEntityCapabilityRegistrar;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.IModHelpers;
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
            add(net.neoforged.neoforge.capabilities.Capabilities.Item.BLOCK, (blockEntity, direction) -> new WorldlyContainerWrapper(blockEntity.getInventory(), direction));
        }

        public void registerTankInventoryCapabilitiesFluid() {
            add(net.neoforged.neoforge.capabilities.Capabilities.Fluid.BLOCK, (blockEntity, direction) -> blockEntity.getTank());
        }
    }

    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize);
    }

    protected SingleUseTank createTank(int tankSize) {
        return new SingleUseTank(tankSize);
    }

    @Override
    public void read(ValueInput valueInput) {
        super.read(valueInput);
        inventory.readFromNBT(valueInput, "inventory");
        tank.deserialize(valueInput, "tank");
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput) {
        inventory.writeToNBT(valueOutput, "inventory");
        tank.serialize(valueOutput, "tank");
        super.saveAdditional(valueOutput);
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

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        IModHelpers.get().getInventoryHelpers().dropItems(level, this.getInventory(), pos);
    }
}
