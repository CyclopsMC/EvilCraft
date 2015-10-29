package org.cyclops.evilcraft.tileentity.tickaction;

import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;

/**
 * Abstract {@link ITickAction} for emptying items in tanks.
 * @author rubensworks
 *
 * @param <T> Extension of {@link TickingTankInventoryTileEntity} that has a tank.
 */
public abstract class EmptyInTankTickAction<T extends TickingTankInventoryTileEntity<T>> implements ITickAction<T> {
    
    protected final static int MB_PER_TICK = GeneralConfig.mbFlowRate;
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isFull()
                && itemStack != null;
    }
    
}
