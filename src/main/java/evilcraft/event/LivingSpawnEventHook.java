package evilcraft.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.entity.monster.PoisonousLibelle;
import evilcraft.entity.monster.PoisonousLibelleConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

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
        if(event.entityLiving instanceof PoisonousLibelle) {
            if(((PoisonousLibelle) event.entityLiving).posY < PoisonousLibelleConfig.minY) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
    
}
