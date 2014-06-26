package evilcraft.events;

import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.VersionStats;

/**
 * Event hook for {@link BonemealEvent}.
 * @author rubensworks
 *
 */
public class PlayerTickEventHook {

    /**
     * When a player tick event is received.
     * @param event The received event.
     */
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onTick(PlayerTickEvent event) {
        versionCheck(event);
    }
    
    private void versionCheck(PlayerTickEvent event) {
        VersionStats.check(event);
    }
    
}
