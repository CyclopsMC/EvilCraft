package org.cyclops.evilcraft.core.tileentity.tickaction;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;

import java.util.Map;
import java.util.Map.Entry;

/**
 * A component used in {@link TickingTankInventoryTileEntity} to support handling of ITickActions.
 * A new instance can be created with a dictionary of (an extension of) {@link ITickAction}.
 * It must be used like {@link TickingTankInventoryTileEntity#update()}.
 * It will then tick for the given tile and slot and check the current item inside
 * the given slot. It will then take the class of that item and lookup the item class inside
 * that map to get the correct {@link ITickAction} on which it will call the methods.
 * @author rubensworks
 * @param <C> A tile that has a consume and produce slot.
 * @param <T> The type of tick action that this component has to tick for.
 * @see ITickAction
 * @see TickingTankInventoryTileEntity
 */
public class TickComponent<C extends CyclopsTileEntity, T extends ITickAction<C>> {
    
    private Map<Class<?>, T> tickActions;
    
    private C tile;

    private boolean redstoneDisableable;
    private int tick = 0;
    
    private float requiredTicks = 0;
    private int slot;
    
    /**
     * Make a new TickComponent.
     * @param tile The IConsumeProduceTile reference in which this ticker runs.
     * @param tickActions The collection of actions this ticker can perform.
     * It must map the item class to an extension of {@link ITickAction}.
     * @param slot The inventory slot this ticker applies to.
     * @param redstoneDisableable If this ticker can be disabled when given a redstone signal.
     */
    public TickComponent(C tile, Map<Class<?>, T> tickActions, int slot, boolean redstoneDisableable) {
        this.tile = tile;
        this.tickActions = tickActions;
        this.slot = slot;
        this.redstoneDisableable = redstoneDisableable;
    }

    /**
     * Make a new TickComponent that can be disabled with redstone.
     * @param tile The IConsumeProduceTile reference in which this ticker runs.
     * @param tickActions The collection of actions this ticker can perform.
     * It must map the item class to an extension of {@link ITickAction}.
     * @param slot The inventory slot this ticker applies to.
     */
    public TickComponent(C tile, Map<Class<?>, T> tickActions, int slot) {
        this(tile, tickActions, slot, true);
    }

    public T getTickAction(Item item) {
        for(Entry<Class<?>, T> entry : tickActions.entrySet()) {
        	Object instance = item;
            if(entry.getKey().isInstance(instance)) {
                return entry.getValue();
            } else {
            	if(item instanceof ItemBlock) {
            		instance = ((ItemBlock) item).getBlock();
            		if(entry.getKey().isInstance(instance)) {
                        return entry.getValue();
            		}
            	}
            }
        }
        return null;
    }
    
    /**
     * Add one tick.
     * @param itemStack The itemStack that is currently inside the slot for this ticker.
     * @param slot The slot id for the ticker.
     */
    public void tick(ItemStack itemStack, int slot) {
        if(itemStack != null) {
            T action = getTickAction(itemStack.getItem());
            if(action != null){
                if(action.canTick(tile, itemStack, slot, tick)) {
                    if (tick == 0)
                        requiredTicks = action.getRequiredTicks(tile, slot, tick);
                    tick++;
                    // We repeat the tick when requiredTicks is smaller than one.
                    int repeatFor = requiredTicks == 0 ? 1 : Math.max(1, (int) Math.ceil((float) 1 / requiredTicks));
                    for(int i = 0; i < repeatFor; i++) {
                        action.onTick(tile, itemStack, slot, tick);
                    }
                    if (tick >= requiredTicks)
                        tick = 0;
                }
            } else {
                tick = 0;
            }
        } else tick = 0;
    }
    
    /**
     * The current tick progress.
     * @return Current tick.
     */
    public int getTick() {
        return tick;
    }
    
    /**
     * Set the current tick.
     * @param tick New tick.
     */
    public void setTick(int tick) {
        this.tick = tick;
    }
    
    /**
     * Get the required ticks for this ticker, will be zero if not defined.
     * @return Required ticks.
     */
    public float getRequiredTicks() {
        return requiredTicks;
    }

    /**
     * Get the slot this ticker applies to.
     * @return The slot.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Set the required ticks, should only be called client side, for display purposes.
     * @param requiredTicks The new required ticks.
     */
    public void setRequiredTicks(float requiredTicks) {
        this.requiredTicks = requiredTicks;
    }

    /**
     * @return If this component is disableable with a redstone signal.
     */
    public boolean isRedstoneDisableable() {
        return redstoneDisableable;
    }
    
}
