package org.cyclops.evilcraft.core.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import javax.annotation.Nullable;

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

        addItemHandlerCapabilities();
        addFluidHandlerCapabilities();

        // Add update listeners
        inventory.addDirtyMarkListener(this);
        tank.addDirtyMarkListener(this::onTankChanged);
    }

    protected void addItemHandlerCapabilities() {
        for (Direction side : Direction.values()) {
            addCapabilityInternal(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(() -> inventory.getItemHandlerSided(side)));
        }
    }

    protected void addFluidHandlerCapabilities() {
        addCapabilityInternal(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, LazyOptional.of(() -> tank));
    }

    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize);
    }

    protected SingleUseTank createTank(int tankSize) {
        return new SingleUseTank(tankSize);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        inventory.readFromNBT(tag, "inventory");
        tank.readFromNBT(tag, "tank");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        inventory.writeToNBT(tag, "inventory");
        tank.writeToNBT(tag, "tank");
        super.saveAdditional(tag);
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
