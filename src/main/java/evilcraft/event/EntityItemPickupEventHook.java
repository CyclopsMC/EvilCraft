package evilcraft.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Achievements;
import evilcraft.item.OriginsOfDarkness;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

/**
 * Event hook for {@link net.minecraftforge.event.entity.player}.
 * @author rubensworks
 *
 */
public class EntityItemPickupEventHook {

    /**
     * When a player tick event is received.
     * @param event The received event.
     */
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onTick(EntityItemPickupEvent event) {
        originsOfDarknessObtain(event);
    }
    
    private void originsOfDarknessObtain(EntityItemPickupEvent event) {
        if(event.entityPlayer != null && event.item != null && event.item.getEntityItem().getItem() == OriginsOfDarkness.getInstance()) {
            event.entityPlayer.addStat(Achievements.EVIL_SOURCE, 1);
        }
    }
    
}
