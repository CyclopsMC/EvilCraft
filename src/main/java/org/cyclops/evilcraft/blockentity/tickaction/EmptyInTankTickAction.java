package org.cyclops.evilcraft.blockentity.tickaction;

import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;

/**
 * Abstract {@link ITickAction} for emptying items in tanks.
 * @author rubensworks
 *
 * @param <T> Extension of {@link BlockEntityTickingTankInventory} that has a tank.
 */
public abstract class EmptyInTankTickAction<T extends BlockEntityTickingTankInventory<T>> implements ITickAction<T> {

    protected final static int MB_PER_TICK = GeneralConfig.mbFlowRate;

    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isFull()
                && !itemStack.isEmpty();
    }

}
