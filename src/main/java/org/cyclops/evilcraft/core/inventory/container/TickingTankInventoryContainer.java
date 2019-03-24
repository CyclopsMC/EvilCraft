package org.cyclops.evilcraft.core.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.inventory.container.TileInventoryContainer;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * A container for a ticking tile entity with inventory.
 * @author rubensworks
 *
 * @param <T> The TickingTankInventoryTileEntity class.
 */
public class TickingTankInventoryContainer<T extends TickingTankInventoryTileEntity<T>> extends TileInventoryContainer<T> {

    private final List<Supplier<Integer>> variablesMaxProgress;
    private final List<Supplier<Integer>> variablesProgress;
    private final Supplier<FluidStack> variableFluidStack;
    private final Supplier<Integer> variableFluidCapacity;
    
    /**
     * Make a new TickingInventoryContainer.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public TickingTankInventoryContainer(InventoryPlayer inventory, T tile) {
        super(inventory, tile);
        this.variablesMaxProgress = Lists.newArrayList();
        this.variablesProgress = Lists.newArrayList();
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            this.variablesMaxProgress.add(registerSyncedVariable(Integer.class, () -> (int) ticker.getRequiredTicks()));
            this.variablesProgress.add(registerSyncedVariable(Integer.class, ticker::getTick));
        }
        this.variableFluidStack = registerSyncedVariable(FluidStack.class, () -> getTile().getTank().getFluid());
        this.variableFluidCapacity = registerSyncedVariable(Integer.class, () -> getTile().getTank().getCapacity());
    }

    public int getMaxProgress(int ticker) {
        return variablesMaxProgress.get(ticker).get();
    }

    public int getProgress(int ticker) {
        return variablesProgress.get(ticker).get();
    }

    @Nullable
    public FluidStack getFluidStack() {
        return variableFluidStack.get();
    }

    public int getFluidCapacity() {
        return variableFluidCapacity.get();
    }

}
