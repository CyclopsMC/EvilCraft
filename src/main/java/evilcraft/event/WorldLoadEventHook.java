package evilcraft.event;

import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.core.fluid.WorldSharedTankCache;

/**
 * Event hook for {@link WorldEvent}.
 * @author rubensworks
 *
 */
public class WorldLoadEventHook {

    /**
     * When a world load event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBonemeal(WorldEvent.Load event) {
        resetWorldSharedTankCache(event);
    }
    
    private void resetWorldSharedTankCache(WorldEvent.Load event) {
        if(event.world.isRemote) {
            WorldSharedTankCache.getInstance().reset();
        }
    }
    
}
