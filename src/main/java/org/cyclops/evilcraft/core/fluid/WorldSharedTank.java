package org.cyclops.evilcraft.core.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.world.WorldStorage;

/**
 * A tank that has shared contents for a given ID.
 * Based on World NBT storage.
 * @author rubensworks
 *
 */
public class WorldSharedTank extends SingleUseTank {

/**
     * The NBT name for the fluid tank.
     */
    public static final String NBT_TANKID = "tankID";

    protected String tankID = "";
    private int previousAmount = 0;

    public WorldSharedTank(int capacity) {
        super(capacity);
    }

    /**
     * Reset the previous fluid storage, used for interpolating fluid amounts client-side.
     */
    public void resetPreviousFluid() {
        previousAmount = getFluidAmount();
    }

    /**
     * Get the previous fluid amount, used for interpolating fluid amounts client-side.
     * @return The previous amount.
     */
    public int getPreviousAmount() {
        return previousAmount;
    }

    @Override
    public void writeTankToNBT(CompoundTag nbt) {
        super.writeTankToNBT(nbt);
        nbt.putString(NBT_TANKID, tankID);
    }

    @Override
    public void readTankFromNBT(CompoundTag nbt) {
        super.readTankFromNBT(nbt);
        tankID = nbt.getString(NBT_TANKID);
    }

    protected void readWorldFluid() {
        this.fluid = WorldSharedTankCache.getInstance().getTankContent(tankID);
    }

    protected void writeWorldFluid() {
        if (!MinecraftHelpers.isClientSideThread()) {
            WorldSharedTankCache.getInstance().setTankContent(tankID, this.fluid);
        }
    }

    @Override
    public void setFluid(FluidStack fluid) {
        super.setFluid(fluid);
        writeWorldFluid();
    }

    @Override
    public FluidStack getFluid() {
        readWorldFluid();
        return super.getFluid();
    }

    @Override
    public int getFluidAmount() {
        readWorldFluid();
        return super.getFluidAmount();
    }

    @Override
    public int getSpace() {
        readWorldFluid();
        return super.getSpace();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        readWorldFluid();
        int ret = super.fill(resource, action);
        if (ret > 0 && action.execute()) {
            writeWorldFluid();
        }
        return ret;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        readWorldFluid();
        FluidStack ret = super.drain(maxDrain, action);
        if (!ret.isEmpty() && action.execute()) {
            writeWorldFluid();
        }
        return ret;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        readWorldFluid();
        FluidStack ret = super.drain(resource, action);
        if ( !ret.isEmpty() && action.execute()) {
            writeWorldFluid();
        }
        return ret;
    }

    /**
     * Get the tank ID.
     * @return The tank ID.
     */
    public String getTankID() {
        return this.tankID;
    }

    /**
     * Set the tank ID.
     * @param tankID The new tank ID.
     */
    public void setTankID(String tankID) {
        this.tankID = tankID;
    }

    @Override
    public Fluid getAcceptedFluid() {
        Fluid fluid = this.getFluidType();
        return fluid == null ? Fluids.EMPTY : fluid;
    }

    @Override
    protected boolean replaceInnerFluid() {
        return false;
    }

    /**
     * Tank data stored in the world.
     * @author rubensworks
     */
    public static class TankData extends WorldStorage {

        /**
         * NBT key.
         */
        public static final String KEY = "WorldSharedTanks";

        /**
         * Make a new instance.
         * @param mod The mod.
         */
        public TankData(ModBase mod) {
            super(mod);
        }

        @Override
        public void reset() {
            WorldSharedTankCache.getInstance().reset();
        }

        @Override
        protected String getDataId() {
            return KEY;
        }

        @Override
        public void readFromNBT(CompoundTag tag) {
            super.readFromNBT(tag);
            WorldSharedTankCache.getInstance().readFromNBT(tag);
        }

        @Override
        public void writeToNBT(CompoundTag tag) {
            super.writeToNBT(tag);
            WorldSharedTankCache.getInstance().writeToNBT(tag);
        }

    }

}
