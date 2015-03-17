package evilcraft.event;

import evilcraft.Configs;
import evilcraft.item.EnderTearConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Event for {@link net.minecraftforge.event.entity.living.LivingDropsEvent}.
 * @author rubensworks
 *
 */
public class LivingDropsEventHook {

    /**
     * When a living death event is received.
     * @param event The received event.
     */
	@SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDrops(LivingDropsEvent event) {
        dropEnderTear(event);
    }

	private void dropEnderTear(LivingDropsEvent event) {
        Entity e = event.entity;
        if(!event.entity.worldObj.isRemote && e instanceof EntityEnderman && Configs.isEnabled(EnderTearConfig.class)) {
            int chance = EnderTearConfig.chanceDrop;
            if(event.lootingLevel > 0) {
                chance /= event.lootingLevel + 1;
            }
            if(chance > 0 && e.worldObj.rand.nextInt(chance) == 0) {
                EntityItem entity = new EntityItem(e.worldObj, e.posX, e.posY, e.posZ, new ItemStack(EnderTearConfig._instance.getItemInstance()));
                entity.setPickupDelay(10);
                event.drops.add(entity);
            }
        }
    }
    
}
