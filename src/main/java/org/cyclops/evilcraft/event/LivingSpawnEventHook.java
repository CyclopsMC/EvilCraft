package org.cyclops.evilcraft.event;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.entity.monster.EntityPoisonousLibelle;
import org.cyclops.evilcraft.entity.monster.EntityPoisonousLibelleConfig;

/**
 * Event hook for {@link net.minecraftforge.event.entity.living.LivingSpawnEvent}.
 * @author rubensworks
 *
 */
public class LivingSpawnEventHook {

    /**
     * When a check spawn event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        checkLibelleSpawn(event);
    }

    private void checkLibelleSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(event.getEntityLiving() instanceof EntityPoisonousLibelle) {
            if(((EntityPoisonousLibelle) event.getEntityLiving()).getPosY() < EntityPoisonousLibelleConfig.minY) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
    
}
