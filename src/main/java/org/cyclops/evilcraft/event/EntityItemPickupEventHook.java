package org.cyclops.evilcraft.event;

import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.item.OriginsOfDarkness;

/**
 * Event hook for {@link net.minecraftforge.event.entity.player}.
 * @author rubensworks
 *
 */
public class EntityItemPickupEventHook {

    /**
     * When a player tick event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onTick(EntityItemPickupEvent event) {
        originsOfDarknessObtain(event);
    }
    
    private void originsOfDarknessObtain(EntityItemPickupEvent event) {
        if(event.getEntityPlayer() != null && event.getItem() != null && event.getItem().getEntityItem().getItem() == OriginsOfDarkness.getInstance()) {
            event.getEntityPlayer().addStat(Achievements.EVIL_SOURCE, 1);
        }
    }
    
}
