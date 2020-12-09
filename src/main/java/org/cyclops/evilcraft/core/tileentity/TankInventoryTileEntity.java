package org.cyclops.evilcraft.core.tileentity;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class TankInventoryTileEntity extends CyclopsTileEntity {

    private final SimpleInventory inventory;
    private final SingleUseTank tank;
    protected final int tankSize;

    public TankInventoryTileEntity(TileEntityType<?> type, int inventorySize, int stackSize, int tankSize, @Nullable Fluid acceptedFluid) {
        super(type);
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
    public void read(CompoundNBT tag) {
        super.read(tag);
        inventory.readFromNBT(tag, "inventory");
        tank.readFromNBT(tag, "tank");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        inventory.writeToNBT(tag, "inventory");
        tank.writeToNBT(tag, "tank");
        return super.write(tag);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public SingleUseTank getTank() {
        return tank;
    }

    public void onTankChanged() {
        markDirty();
        getInventory().markDirty();
    }
}
