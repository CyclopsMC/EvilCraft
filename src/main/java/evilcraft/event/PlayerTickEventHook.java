package evilcraft.event;

import evilcraft.VersionStats;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
