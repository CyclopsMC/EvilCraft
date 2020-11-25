package org.cyclops.evilcraft.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.GeneralConfig;

/**
 * Event hook for {@link LivingUpdateEvent}.
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
    public void tick(LivingUpdateEvent event) {
        dieWithoutAnyReason(event);
    }

    private void dieWithoutAnyReason(LivingUpdateEvent event) {
        if(event.getEntity() instanceof PlayerEntity && GeneralConfig.dieWithoutAnyReason
        		&& event.getEntity().world.rand.nextInt(CHANCE_DIE_WITHOUT_ANY_REASON) == 0
        		&& !event.getEntity().world.isRemote()) {
            PlayerEntity entity = (PlayerEntity) event.getEntity();
            entity.attackEntityFrom(ExtendedDamageSource.dieWithoutAnyReason, Float.MAX_VALUE);
        }
    }
    
}
