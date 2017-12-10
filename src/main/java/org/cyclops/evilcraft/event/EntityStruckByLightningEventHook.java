package org.cyclops.evilcraft.event;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.entity.villager.WerewolfVillager;
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
    public void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
		empowerItem(event);
		transformVillager(event);
    }
    
    private void empowerItem(EntityStruckByLightningEvent event) {
        if(event.getEntity() instanceof EntityItem) {
            EntityItem entity = (EntityItem) event.getEntity();
            if(entity.getItem().getItem() instanceof IItemEmpowerable) {
            	IItemEmpowerable empowerable = (IItemEmpowerable) entity.getItem().getItem();
            	if(!empowerable.isEmpowered(entity.getItem())) {
            		entity.setItem(empowerable.empower(entity.getItem()));
            		event.setCanceled(true);
            		event.getLightning().setDead();
            	}
            }
        }
    }
    
    private EntityLightningBolt lastLightningBolt;
    private Set<EntityVillager> affectedVillagers;

    private void transformVillager(EntityStruckByLightningEvent event) {
        if(Configs.isEnabled(WerewolfVillagerConfig.class) && event.getEntity() instanceof EntityVillager) {
            EntityVillager entity = (EntityVillager) event.getEntity();
            if(lastLightningBolt != event.getLightning()) {
                lastLightningBolt = event.getLightning();
                affectedVillagers = new HashSet<>();
            }
            if(!affectedVillagers.add(entity)) {
                // The same lightning bolt will strike multiple times. Only count the first time it hits any entity
                event.setCanceled(true);
                return;
            }
            if(entity.getProfessionForge() != WerewolfVillager.getInstance()) {
            	entity.setProfession(WerewolfVillager.getInstance());
            }
            if(entity.getWorld().rand.nextBoolean())
                event.setCanceled(true); // 50% chance that they become a witch like vanilla does
        }
    }
    
}
