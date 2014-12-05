package evilcraft.tileentity.tickaction;

import evilcraft.GeneralConfig;
import evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import evilcraft.core.tileentity.tickaction.ITickAction;
import net.minecraft.item.ItemStack;

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
                && itemStack != null
                && itemStack.stackSize == 1;
    }
    
}
