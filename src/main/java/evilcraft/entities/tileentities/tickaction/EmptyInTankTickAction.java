package evilcraft.entities.tileentities.tickaction;

import net.minecraft.item.ItemStack;
import evilcraft.core.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.core.entities.tileentitites.tickaction.ITickAction;

/**
 * Abstract {@link ITickAction} for emptying items in tanks.
 * @author rubensworks
 *
 * @param <T> Extension of {@link TickingTankInventoryTileEntity} that has a tank.
 */
public abstract class EmptyInTankTickAction<T extends TickingTankInventoryTileEntity<T>> implements ITickAction<T> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isFull()
                && itemStack != null
                && itemStack.stackSize == 1;
    }
    
}
