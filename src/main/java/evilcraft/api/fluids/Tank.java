package evilcraft.api.fluids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

/**
 * A simple fluid tank.
 * Based on the Buildcraft Tank
 * @author rubensworks
 *
 */
public class Tank extends FluidTank {

    private final String name;

    /**
     * Make a new fluid tank.
     * @param name The name for the tank, used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The {@link TileEntity} that uses this tank.
     */
    public Tank(String name, int capacity, TileEntity tile) {
        super(capacity);
        this.name = name;
        this.tile = tile;
    }
    
    /**
     * Check if this tank is empty.
     * @return If the tank is empty; no fluid is inside of it.
     */
    public boolean isEmpty() {
        return getFluid() == null || getFluid().amount <= 0;
    }

    /**
     * Check if this tank is full; the capacity is reached.
     * @return If this tank is full.
     */
    public boolean isFull() {
        return getFluid() != null && getFluid().amount >= getCapacity();
    }

    /**
     * Get the fluid that currently occupies this tank, will return null if there is no fluid.
     * @return The inner fluid.
     */
    public Fluid getFluidType() {
        return getFluid() != null ? getFluid().getFluid() : null;
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tankData = new NBTTagCompound();
        super.writeToNBT(tankData);
        writeTankToNBT(tankData);
        nbt.setTag(name, tankData);
        return nbt;
    }

    @Override
    public final FluidTank readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(name)) {
            NBTTagCompound tankData = nbt.getCompoundTag(name);
            super.readFromNBT(tankData);
            readTankFromNBT(tankData);
        }
        return this;
    }

    /**
     * Write the tank contents to NBT.
     * @param nbt The NBT tag to write to.
     */
    public void writeTankToNBT(NBTTagCompound nbt) {}

    /**
     * Read the tank contents from NBT.
     * @param nbt The NBT tag to write from.
     */
    public void readTankFromNBT(NBTTagCompound nbt) {}

}
