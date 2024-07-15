package org.cyclops.evilcraft.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.cyclops.evilcraft.ExtendedDamageSources;
import org.cyclops.evilcraft.GeneralConfig;

/**
 * Event hook for {@link EntityTickEvent}.
 * @author rubensworks
 *
 */
public class LivingUpdateEventHook {

    private static final int CHANCE_DIE_WITHOUT_ANY_REASON = 1000000; // Real chance is 1/CHANCE_DIE_WITHOUT_ANY_REASON

    /**
     * When a sound event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void tick(EntityTickEvent.Post event) {
        dieWithoutAnyReason(event);
    }

    private void dieWithoutAnyReason(EntityTickEvent.Post event) {
        if(event.getEntity() instanceof Player && GeneralConfig.dieWithoutAnyReason
                && event.getEntity().level().random.nextInt(CHANCE_DIE_WITHOUT_ANY_REASON) == 0
                && !event.getEntity().level().isClientSide()) {
            Player entity = (Player) event.getEntity();
            entity.hurt(ExtendedDamageSources.dieWithoutAnyReason(entity.level()), Float.MAX_VALUE);
        }
    }

}
