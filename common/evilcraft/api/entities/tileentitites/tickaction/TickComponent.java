package evilcraft.api.entities.tileentitites.tickaction;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.IConsumeProduceTile;

/**
 * A component used in TileEntities to support handling of ITickActions.
 * @author rubensworks
 * @param <C> A tile that has a consume and produce slot.
 * @param <T> The type of tick action that this component has to tick for.
 *
 */
public class TickComponent<C extends IConsumeProduceTile, T extends ITickAction<C>> {
    
    private Map<Class<?>, T> tickActions;
    
    private C tile;
    private int tick = 0;
    
    private boolean setRequiredTicks = false;
    private int requiredTicks = 0;
    
    public TickComponent(C tile, Map<Class<?>, T> tickActions, boolean setRequiredTick) {
        this.tile = tile;
        this.tickActions = tickActions;
        this.setRequiredTicks = setRequiredTick;
    }
    
    public TickComponent(C tile, Map<Class<?>, T> tickActions) {
        this(tile, tickActions, false);
    }

    protected T getTickAction(Item item) {
        for(Entry<Class<?>, T> entry : tickActions.entrySet()) {
            if(entry.getKey().isInstance(item)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    public void tick(ItemStack itemStack) {
        if(itemStack != null) {
            T action = getTickAction(itemStack.getItem());
            if(action.canTick(tile, tick)){
                if(tick == 0 && setRequiredTicks)
                    requiredTicks = action.getRequiredTicks(tile);
                tick++;
                action.onTick(tile, tick);
            } else {
                tick = 0;
            }
        } else tick = 0;
    }
    
    public int getTick() {
        return tick;
    }
    
    public void setTick(int tick) {
        this.tick = tick;
    }
    
    public int getRequiredTicks() {
        return requiredTicks;
    }
    
}
