package org.cyclops.evilcraft.core.fluid;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import org.cyclops.cyclopscore.helper.Helpers;

import javax.annotation.Nullable;

/**
 * A tank that acts as a proxy to a collection of other tanks.
 * @author Ruben Taelman
 */
public class VirtualTank implements IFluidTank {

    public static final EnumFacing TARGETSIDE = EnumFacing.UP;

    private final ITankProvider tankProvider;
    private final boolean spreadEvenly;

    /**
     * Make a new tank instance.
     * @param tankProvider The dynamic provider of tanks.
     * @param spreadEvenly If the filling and draining should happen evenly across all tanks.
     */
    public VirtualTank(ITankProvider tankProvider, boolean spreadEvenly) {
        this.tankProvider = tankProvider;
        this.spreadEvenly = spreadEvenly;
    }

    protected IFluidHandler[] getTanks() {
        IFluidHandler[] tanks = tankProvider.getVirtualTankChildren();
        if(tanks == null) {
            tanks = new IFluidHandler[0];
        }
        return tanks;
    }

    @Override
    public FluidStack getFluid() {
        if(isSpreadEvenly()) {
            FluidStack minFluid = null;
            int min = Integer.MAX_VALUE;
            for (IFluidHandler tank : getTanks()) {
                for (FluidTankInfo info : tank.getTankInfo(TARGETSIDE)) {
                    FluidStack tankFluid = info.fluid;
                    if (tankFluid != null) {
                        if(tankFluid.amount < min) {
                            min = tankFluid.amount;
                            minFluid = tankFluid;
                        }
                    }
                }
            }
            return minFluid == null ? null : new FluidStack(minFluid.getFluid(), min * getTanks().length);
        } else {
            FluidStack total = null;
            for (IFluidHandler tank : getTanks()) {
                for (FluidTankInfo info : tank.getTankInfo(TARGETSIDE)) {
                    FluidStack tankFluid = info.fluid;
                    if (tankFluid != null) {
                        if (total == null) {
                            total = tankFluid.copy();
                        } else if (total.getFluid() == tankFluid.getFluid()) {
                            total = new FluidStack(total.getFluid(), total.amount + tankFluid.amount);
                        }
                    }
                }
            }
            return total;
        }
    }

    @Override
    public int getFluidAmount() {
        FluidStack fluidStack = getFluid();
        return fluidStack == null ? 0 : fluidStack.amount;
    }

    @Override
    public int getCapacity() {
        int total = 0;
        for (IFluidHandler tank : getTanks()) {
            for (FluidTankInfo info : tank.getTankInfo(TARGETSIDE)) {
                total = Helpers.addSafe(total, info.capacity);
            }
        }
        return total;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        FluidStack toFill = resource.copy();
        int totalFilled = 0;
        int tanks = getTanks().length;
        IFluidHandler[] tanksArray = getTanks();
        for (int i = 0; i < tanks; i++) {
            IFluidHandler tank = tanksArray[i];
            if (isSpreadEvenly()) {
                toFill = resource.copy();
                resource.amount = resource.amount / tanks + ((i <= resource.amount % tanks) ? 1 : 0);
            }
            int filled = tank.fill(TARGETSIDE, toFill, doFill);
            toFill = toFill.copy();
            toFill.amount -= filled;
            totalFilled += filled;
            if (totalFilled == resource.amount) {
                return totalFilled;
            }
        }
        return totalFilled;
    }

    protected boolean isSpreadEvenly() {
        return this.spreadEvenly;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        int toDrain = maxDrain;
        FluidStack totalDrained = null;
        int tanks = getTanks().length;
        IFluidHandler[] tanksArray = getTanks();
        for (int i = 0; i < tanks; i++) {
            if (isSpreadEvenly()) {
                toDrain = maxDrain / tanks + ((i <= maxDrain % tanks) ? 1 : 0);
            }
            IFluidHandler tank = tanksArray[i];
            FluidStack drained = tank.drain(TARGETSIDE, toDrain, doDrain);
            if (drained != null) {
                toDrain -= drained.amount;
                if (totalDrained == null) {
                    totalDrained = drained;
                } else {
                    totalDrained.amount += drained.amount;
                }
                if (totalDrained.amount == maxDrain) {
                    return totalDrained;
                }
            }
        }
        return totalDrained;
    }

    public interface ITankProvider {

        public @Nullable IFluidHandler[] getVirtualTankChildren();

    }

}
