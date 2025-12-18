package org.cyclops.evilcraft.core.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;

/**
 * A tank that acts as a proxy to a collection of other tanks.
 * @author Ruben Taelman
 */
public class VirtualTank implements ResourceHandler<FluidResource> {

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

    protected ResourceHandler<FluidResource>[] getTanks() {
        ResourceHandler<FluidResource>[] tanks = tankProvider.getVirtualTankChildren();
        if(tanks == null) {
            tanks = new ResourceHandler[0];
        }
        return tanks;
    }

    public FluidStack getFluid() {
        if(isSpreadEvenly()) {
            FluidStack minFluid = FluidStack.EMPTY;
            int min = Integer.MAX_VALUE;
            for (ResourceHandler<FluidResource> tank : getTanks()) {
                int tanks = tank.size();
                for (int i = 0; i < tanks; i++) {
                    FluidStack tankFluid = tank.getResource(i).toStack(tank.getAmountAsInt(i));
                    if (!tankFluid.isEmpty()) {
                        if(tankFluid.getAmount() < min) {
                            min = tankFluid.getAmount();
                            minFluid = tankFluid;
                        }
                    }
                }
            }
            return minFluid.isEmpty() ? FluidStack.EMPTY : new FluidStack(minFluid.getFluid(), min * getTanks().length);
        } else {
            FluidStack total = FluidStack.EMPTY;
            for (ResourceHandler<FluidResource> tank : getTanks()) {
                int tanks = tank.size();
                for (int i = 0; i < tanks; i++) {
                    FluidStack tankFluid = tank.getResource(i).toStack(tank.getAmountAsInt(i));
                    if (!tankFluid.isEmpty()) {
                        if (total.isEmpty()) {
                            total = tankFluid.copy();
                        } else if (total.getFluid() == tankFluid.getFluid()) {
                            total = new FluidStack(total.getFluid(), total.getAmount() + tankFluid.getAmount());
                        }
                    }
                }
            }
            return total;
        }
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        return FluidResource.of(getFluid());
    }

    @Override
    public long getAmountAsLong(int i) {
        return getFluid().getAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        long total = 0;
        for (ResourceHandler<FluidResource> tank : getTanks()) {
            int tanks = tank.size();
            for (int i = 0; i < tanks; i++) {
                total += tank.getCapacityAsLong(i, FluidResource.EMPTY);
            }
        }
        return total;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return true;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        int toFill = amount;
        int totalFilled = 0;
        int tanks = getTanks().length;
        ResourceHandler<FluidResource>[] tanksArray = getTanks();
        for (int i = 0; i < tanks; i++) {
            ResourceHandler<FluidResource> tank = tanksArray[i];
            if (isSpreadEvenly()) {
                toFill = amount / tanks + ((i <= amount % tanks) ? 1 : 0);
            }
            int filled = tank.insert(resource, toFill, transaction);
            toFill -= filled;
            totalFilled += filled;
            if (totalFilled == amount) {
                return totalFilled;
            }
        }
        return totalFilled;
    }

    protected boolean isSpreadEvenly() {
        return this.spreadEvenly;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        int toDrain = amount;
        int totalDrained = 0;
        int tanks = getTanks().length;
        ResourceHandler<FluidResource>[] tanksArray = getTanks();
        for (int i = 0; i < tanks; i++) {
            if (isSpreadEvenly()) {
                toDrain = amount / tanks + ((i <= amount % tanks) ? 1 : 0);
            }
            ResourceHandler<FluidResource> tank = tanksArray[i];
            int drained = tank.extract(resource, toDrain, transaction);
            if (drained > 0) {
                toDrain -= drained;
                totalDrained += drained;
                if (totalDrained == amount) {
                    return totalDrained;
                }
            }
        }
        return totalDrained;
    }

    public interface ITankProvider {

        public @Nullable ResourceHandler<FluidResource>[] getVirtualTankChildren();

    }

}
