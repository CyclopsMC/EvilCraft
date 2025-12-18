package org.cyclops.evilcraft.core.fluid;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.IModHelpers;

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
    public void serialize(ValueOutput output) {
        super.serialize(output);
        output.putString(NBT_TANKID, tankID);
    }

    @Override
    public void deserialize(ValueInput input) {
        super.deserialize(input);
        tankID = input.getString(NBT_TANKID).orElseThrow();
    }

    protected void readWorldFluid() {
        this.stacks.set(0, WorldSharedTankCache.getInstance().getTankContent(tankID));
    }

    protected void writeWorldFluid() {
        if (!IModHelpers.get().getMinecraftHelpers().isClientSideThread()) {
            WorldSharedTankCache.getInstance().setTankContent(tankID, this.stacks.get(0));
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
    protected void onContentsChanged(int index, FluidStack previousContents) {
        writeWorldFluid();
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        readWorldFluid();
        return super.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        readWorldFluid();
        return super.extract(index, resource, amount, transaction);
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

}
