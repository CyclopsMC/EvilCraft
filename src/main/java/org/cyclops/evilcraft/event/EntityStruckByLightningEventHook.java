package org.cyclops.evilcraft.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.evilcraft.entity.villager.WerewolfVillagerConfig;
import org.cyclops.evilcraft.item.IItemEmpowerable;

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
		empowerItem(event);
		transformVillager(event);
    }
    
    private void empowerItem(EntityStruckByLightningEvent event) {
        if(event.getEntity() instanceof EntityItem) {
            EntityItem entity = (EntityItem) event.getEntity();
            if(entity.getEntityItem().getItem() instanceof IItemEmpowerable) {
            	IItemEmpowerable empowerable = (IItemEmpowerable) entity.getEntityItem().getItem();
            	if(!empowerable.isEmpowered(entity.getEntityItem())) {
            		entity.setEntityItemStack(empowerable.empower(entity.getEntityItem()));
            		event.setCanceled(true);
            		event.getLightning().setDead();
            	}
            }
        }
    }
    
    private void transformVillager(EntityStruckByLightningEvent event) {
        if(event.getEntity() instanceof EntityVillager) {
        	EntityVillager entity = (EntityVillager) event.getEntity();
            if(entity.getProfession() != WerewolfVillagerConfig.villagerID) {
            	entity.setProfession(WerewolfVillagerConfig.villagerID);
            }
        }
    }
    
}
