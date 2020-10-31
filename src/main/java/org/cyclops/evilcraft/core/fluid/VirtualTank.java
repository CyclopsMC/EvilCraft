package org.cyclops.evilcraft.core.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.Helpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A tank that acts as a proxy to a collection of other tanks.
 * @author Ruben Taelman
 */
public class VirtualTank implements IFluidTank {

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
            FluidStack minFluid = FluidStack.EMPTY;
            int min = Integer.MAX_VALUE;
            for (IFluidHandler tank : getTanks()) {
                int tanks = tank.getTanks();
                for (int i = 0; i < tanks; i++) {
                    FluidStack tankFluid = tank.getFluidInTank(i);
                    if (!tankFluid.isEmpty()) {
                        if(tankFluid.getAmount() < min) {
                            min = tankFluid.getAmount();
                            minFluid = tankFluid;
                        }
                    }
                }
            }
            return minFluid.isEmpty() ? FluidStack.EMPTY : new FluidStack(minFluid, min * getTanks().length);
        } else {
            FluidStack total = FluidStack.EMPTY;
            for (IFluidHandler tank : getTanks()) {
                int tanks = tank.getTanks();
                for (int i = 0; i < tanks; i++) {
                    FluidStack tankFluid = tank.getFluidInTank(i);
                    if (!tankFluid.isEmpty()) {
                        if (total.isEmpty()) {
                            total = tankFluid.copy();
                        } else if (total.getFluid() == tankFluid.getFluid()) {
                            total = new FluidStack(total, total.getAmount() + tankFluid.getAmount());
                        }
                    }
                }
            }
            return total;
        }
    }

    @Override
    public int getFluidAmount() {
        return getFluid().getAmount();
    }

    @Override
    public int getCapacity() {
        int total = 0;
        for (IFluidHandler tank : getTanks()) {
            int tanks = tank.getTanks();
            for (int i = 0; i < tanks; i++) {
                total = Helpers.addSafe(total, tank.getTankCapacity(i));
            }
        }
        return total;
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        FluidStack toFill = resource.copy();
        int totalFilled = 0;
        int tanks = getTanks().length;
        IFluidHandler[] tanksArray = getTanks();
        for (int i = 0; i < tanks; i++) {
            IFluidHandler tank = tanksArray[i];
            if (isSpreadEvenly()) {
                toFill = resource.copy();
                resource.setAmount(resource.getAmount() / tanks + ((i <= resource.getAmount() % tanks) ? 1 : 0));
            }
            int filled = tank.fill(toFill, action);
            toFill = toFill.copy();
            toFill.setAmount(toFill.getAmount() - filled);
            totalFilled += filled;
            if (totalFilled == resource.getAmount()) {
                return totalFilled;
            }
        }
        return totalFilled;
    }

    protected boolean isSpreadEvenly() {
        return this.spreadEvenly;
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        int toDrain = maxDrain;
        FluidStack totalDrained = FluidStack.EMPTY;
        int tanks = getTanks().length;
        IFluidHandler[] tanksArray = getTanks();
        for (int i = 0; i < tanks; i++) {
            if (isSpreadEvenly()) {
                toDrain = maxDrain / tanks + ((i <= maxDrain % tanks) ? 1 : 0);
            }
            IFluidHandler tank = tanksArray[i];
            FluidStack drained = tank.drain(toDrain, action);
            if (!drained.isEmpty()) {
                toDrain -= drained.getAmount();
                if (totalDrained.isEmpty()) {
                    totalDrained = drained;
                } else {
                    totalDrained.setAmount(totalDrained.getAmount() + drained.getAmount());;
                }
                if (totalDrained.getAmount() == maxDrain) {
                    return totalDrained;
                }
            }
        }
        return totalDrained;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        return drain(resource.getAmount(), action);
    }

    public interface ITankProvider {

        public @Nullable IFluidHandler[] getVirtualTankChildren();

    }

}
