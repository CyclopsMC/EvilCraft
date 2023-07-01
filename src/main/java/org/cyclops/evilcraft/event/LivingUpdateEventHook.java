package org.cyclops.evilcraft.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.ExtendedDamageSources;
import org.cyclops.evilcraft.GeneralConfig;

/**
 * Event hook for {@link LivingTickEvent}.
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
    public void tick(LivingTickEvent event) {
        dieWithoutAnyReason(event);
    }

    private void dieWithoutAnyReason(LivingTickEvent event) {
        if(event.getEntity() instanceof Player && GeneralConfig.dieWithoutAnyReason
                && event.getEntity().level().random.nextInt(CHANCE_DIE_WITHOUT_ANY_REASON) == 0
                && !event.getEntity().level().isClientSide()) {
            Player entity = (Player) event.getEntity();
            entity.hurt(ExtendedDamageSources.dieWithoutAnyReason(entity.level()), Float.MAX_VALUE);
        }
    }

}
