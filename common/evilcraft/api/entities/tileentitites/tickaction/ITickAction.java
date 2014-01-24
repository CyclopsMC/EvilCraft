package evilcraft.api.entities.tileentitites.tickaction;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * An action that can be given to TickComponents which are used inside Ticking TileEntities.
 * @author rubensworks
 *
 * @param <T> Subclass of TileEntity
 */
public interface ITickAction<T extends TileEntity> {
    
    /**
     * If this action can tick for the given conditions.
     * @param tile The tile entity that ticks.
     * @param itemStack The ItemStack that is in the slot for which this ticker runs.
     * @param slot The slot ID for the ticker.
     * @param tick The current tick.
     * @return
     */
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick);
    /**
     * Add one tick.
     * @param tile The tile entity that ticks.
     * @param itemStack The ItemStack that is in the slot for which this ticker runs.
     * @param slot The slot ID for the ticker.
     * @param tick The current tick.
     */
    public void onTick(T tile, ItemStack itemStack, int slot, int tick);
    /**
     * Get the required conditions for the given conditions.
     * @param tile The tile entity that ticks.
     * @param slot The slot ID for the ticker.
     * @return
     */
    public int getRequiredTicks(T tile, int slot);
    
}
