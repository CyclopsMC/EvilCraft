package org.cyclops.evilcraft.core.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.inventory.container.InventoryContainer;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A container for a ticking tile entity with inventory.
 * @author rubensworks
 *
 * @param <T> The TickingTankInventoryTileEntity class.
 */
public class ContainerInventoryTickingTank<T extends BlockEntityTickingTankInventory<T>> extends InventoryContainer {

    private final Optional<T> tileSupplier;
    private final List<Supplier<Integer>> variablesMaxProgress;
    private final List<Supplier<Integer>> variablesProgress;
    private final Supplier<FluidStack> variableFluidStack;
    private final Supplier<Integer> variableFluidCapacity;

    public ContainerInventoryTickingTank(@Nullable MenuType<?> type, int id, Inventory playerInventory,
                                         Container inventory, Optional<T> tileSupplier, int tickers) {
        super(type, id, playerInventory, inventory);
        this.variablesMaxProgress = Lists.newArrayList();
        this.variablesProgress = Lists.newArrayList();
        for (int ticker = 0; ticker < tickers; ticker++) {
            int finalTicker = ticker;
            this.variablesMaxProgress.add(registerSyncedVariable(Integer.class, () -> (int) tileSupplier.get().getTickers().get(finalTicker).getRequiredTicks()));
            this.variablesProgress.add(registerSyncedVariable(Integer.class, () -> tileSupplier.get().getTickers().get(finalTicker).getTick()));
        }
        this.variableFluidStack = registerSyncedVariable(FluidStack.class, () -> getTileSupplier().get().getTank().getFluid());
        this.variableFluidCapacity = registerSyncedVariable(Integer.class, () -> getTileSupplier().get().getTank().getCapacity());
        this.tileSupplier = tileSupplier;
    }

    public Optional<T> getTileSupplier() {
        return tileSupplier;
    }

    public int getMaxProgress(int ticker) {
        return variablesMaxProgress.get(ticker).get();
    }

    public int getProgress(int ticker) {
        return variablesProgress.get(ticker).get();
    }

    public FluidStack getFluidStack() {
        FluidStack fluidStack = variableFluidStack.get();
        return fluidStack == null ? FluidStack.EMPTY : fluidStack;
    }

    public int getFluidCapacity() {
        return variableFluidCapacity.get();
    }
}
