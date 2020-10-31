package org.cyclops.evilcraft.event;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.IItemEmpowerable;

import java.util.HashSet;
import java.util.Set;

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
        if(event.getEntity() instanceof ItemEntity) {
            ItemEntity entity = (ItemEntity) event.getEntity();
            if(entity.getItem().getItem() instanceof IItemEmpowerable) {
            	IItemEmpowerable empowerable = (IItemEmpowerable) entity.getItem().getItem();
            	if(!empowerable.isEmpowered(entity.getItem())) {
            		entity.setItem(empowerable.empower(entity.getItem()));
            		event.setCanceled(true);
            		event.getLightning().remove();
            	}
            }
        }
    }
    
    private LightningBoltEntity lastLightningBolt;
    private Set<VillagerEntity> affectedVillagers;

    private void transformVillager(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof VillagerEntity) {
            VillagerEntity entity = (VillagerEntity) event.getEntity();
            if(lastLightningBolt != event.getLightning()) {
                lastLightningBolt = event.getLightning();
                affectedVillagers = new HashSet<>();
            }
            if(!affectedVillagers.add(entity)) {
                // The same lightning bolt will strike multiple times. Only count the first time it hits any entity
                event.setCanceled(true);
                return;
            }
            if(entity.getVillagerData().getProfession() != RegistryEntries.VILLAGER_PROFESSION_WEREWOLF) {
            	entity.setVillagerData(entity
                        .getVillagerData()
                        .withProfession(RegistryEntries.VILLAGER_PROFESSION_WEREWOLF));
            }
            if(entity.getWorld().rand.nextBoolean())
                event.setCanceled(true); // 50% chance that they become a witch like vanilla does
        }
    }
    
}
