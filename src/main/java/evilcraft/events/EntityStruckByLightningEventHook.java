package evilcraft.events;

import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Configs;
import evilcraft.items.InvertedPotentia;
import evilcraft.items.InvertedPotentiaConfig;

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
	@SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingAttack(EntityStruckByLightningEvent event) {
        empowerInvertedPotentia(event);
    }
    
    private void empowerInvertedPotentia(EntityStruckByLightningEvent event) {
        if(event.entity instanceof EntityItem && Configs.isEnabled(InvertedPotentiaConfig.class)) {
            EntityItem entity = (EntityItem) event.entity;
            if(!InvertedPotentia.isEmpowered(entity.getEntityItem())) {
                InvertedPotentia.empower(entity.getEntityItem());

                event.setCanceled(true);
                event.lightning.setDead();
            }
        }
    }
    
}
