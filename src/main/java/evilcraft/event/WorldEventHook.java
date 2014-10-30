package evilcraft.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Event hook for {@link WorldEvent}.
 * @author rubensworks
 *
 */
public class WorldEventHook {

    /**
     * When a world load event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLoadEvent(WorldEvent.Load event) {
        resetCache(event);
    }
    
    private void resetCache(WorldEvent.Load event) {
        if(event.world.isRemote) {
            // TODO: better way of resetting this
            //WorldSharedTankCache.getInstance().reset();
            //GlobalCounter.getInstance().reset();
        }
    }
    
}
