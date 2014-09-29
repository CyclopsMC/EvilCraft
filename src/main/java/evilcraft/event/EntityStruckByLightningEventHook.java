package evilcraft.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.entity.villager.WerewolfVillagerConfig;
import evilcraft.item.IItemEmpowerable;

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
        if(event.entity instanceof EntityItem) {
            EntityItem entity = (EntityItem) event.entity;
            if(entity.getEntityItem().getItem() instanceof IItemEmpowerable) {
            	IItemEmpowerable empowerable = (IItemEmpowerable) entity.getEntityItem().getItem();
            	if(!empowerable.isEmpowered(entity.getEntityItem())) {
            		entity.setEntityItemStack(empowerable.empower(entity.getEntityItem()));
            		event.setCanceled(true);
            		event.lightning.setDead();
            	}
            }
        }
    }
    
    private void transformVillager(EntityStruckByLightningEvent event) {
        if(event.entity instanceof EntityVillager) {
        	EntityVillager entity = (EntityVillager) event.entity;
            if(entity.getProfession() != WerewolfVillagerConfig.villagerID) {
            	entity.setProfession(WerewolfVillagerConfig.villagerID);
            }
        }
    }
    
}
