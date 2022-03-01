package org.cyclops.evilcraft.core.blockentity.tickaction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;

/**
 * An action that can be given to TickComponents which are used inside {@link BlockEntityTickingTankInventory}
 * @author rubensworks
 * @param <T> Subclass of TileEntity
 * @see TickComponent
 */
public interface ITickAction<T extends BlockEntity> {

    /**
     * If this action can tick for the given conditions.
     * @param tile The tile entity that ticks.
     * @param itemStack The ItemStack that is in the slot for which this ticker runs.
     * @param slot The slot ID for the ticker.
     * @param tick The current tick.
     * @return If the action can tick for the given parameters.
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
     * @param tick The current tick.
     * @return Get the required ticks for the given slot for the given tile.
     */
    public float getRequiredTicks(T tile, int slot, int tick);

}
