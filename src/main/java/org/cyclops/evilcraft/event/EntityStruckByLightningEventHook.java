package org.cyclops.evilcraft.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityWerewolf;
import org.cyclops.evilcraft.entity.villager.VillagerProfessionWerewolfConfig;
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
                    event.getLightning().remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private LightningBolt lastLightningBolt;
    private Set<Integer> affectedVillagers;

    private void transformVillager(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof Villager && VillagerProfessionWerewolfConfig.convertOnLightning) {
            Villager entity = (Villager) event.getEntity();
            if(lastLightningBolt != event.getLightning()) {
                lastLightningBolt = event.getLightning();
                affectedVillagers = new HashSet<>();
            }
            if(!affectedVillagers.add(entity.getId())) {
                // The same lightning bolt will strike multiple times. Only count the first time it hits any entity
                event.setCanceled(true);
                return;
            }
            if(entity.level().random.nextBoolean()) {
                event.setCanceled(false);
                return; // 50% chance that they become a witch like vanilla does
            }
            if(entity.getVillagerData().getProfession() != RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.get()) {
                event.setCanceled(true);
                EntityWerewolf.initializeWerewolfVillagerData(entity);
            }
        }
    }

}
