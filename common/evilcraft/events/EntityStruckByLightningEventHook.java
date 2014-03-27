package evilcraft.events;

import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import evilcraft.items.InvertedPotentia;

/**
 * Event hook for {@link EntityStruckByLightningEvent}.
 * @author rubensworks
 *
 */
public class EntityStruckByLightningEventHook {

    /**
     * When a living attack event is received.
     * @param event The received event.
     */
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void onLivingAttack(EntityStruckByLightningEvent event) {
        empowerInvertedPotentia(event);
    }
    
    private void empowerInvertedPotentia(EntityStruckByLightningEvent event) {
        if(event.entity instanceof EntityItem) {
            EntityItem entity = (EntityItem) event.entity;
            if(!InvertedPotentia.isEmpowered(entity.getEntityItem())) {
                InvertedPotentia.empower(entity.getEntityItem());

                event.setCanceled(true);
                event.lightning.setDead();
            }
        }
    }
    
}
