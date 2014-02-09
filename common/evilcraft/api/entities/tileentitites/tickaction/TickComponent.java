package evilcraft.api.entities.tileentitites.tickaction;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;

/**
 * A component used in {@link TickingTankInventoryTileEntity} to support handling of ITickActions.
 * A new instance can be created with a dictionary of (an extension of) {@link ITickAction}.
 * It must be used like {@link TickingTankInventoryTileEntity#updateEntity()}.
 * It will then tick for the given tile and slot and check the current item inside
 * the given slot. It will then take the class of that item and lookup the item class inside
 * that map to get the correct {@link ITickAction} on which it will call the methods.
 * @author rubensworks
 * @param <C> A tile that has a consume and produce slot.
 * @param <T> The type of tick action that this component has to tick for.
 * @see ITickAction
 * @see TickingTankInventoryTileEntity
 */
public class TickComponent<C extends EvilCraftTileEntity, T extends ITickAction<C>> {
    
    private Map<Class<?>, T> tickActions;
    
    private C tile;
    private int tick = 0;
    
    private int requiredTicks = 0;
    private int slot;
    
    /**
     * Make a new TickComponent.
     * @param tile The IConsumeProduceTile reference in which this ticker runs.
     * @param tickActions The collection of actions this ticker can perform.
     * It must map the item class to an extension of {@link ITickAction}.
     * @param slot The inventory slot this ticker applies to.
     */
    public TickComponent(C tile, Map<Class<?>, T> tickActions, int slot) {
        this.tile = tile;
        this.tickActions = tickActions;
        this.slot = slot;
    }

    protected T getTickAction(Item item) {
        for(Entry<Class<?>, T> entry : tickActions.entrySet()) {
            if(entry.getKey().isInstance(item)) {
                return entry.getValue();
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
            if(action != null && action.canTick(tile, itemStack, slot, tick)){
                if(tick == 0)
                    requiredTicks = action.getRequiredTicks(tile, slot);
                tick++;
                if(tick > requiredTicks)
                    tick = 0;
                action.onTick(tile, itemStack, slot, tick);
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
    public int getRequiredTicks() {
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
    public void setRequiredTicks(int requiredTicks) {
        this.requiredTicks = requiredTicks;
    }
    
}
