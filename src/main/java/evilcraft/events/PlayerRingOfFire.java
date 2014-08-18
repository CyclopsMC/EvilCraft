package evilcraft.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import evilcraft.api.algorithms.Location;
import evilcraft.api.helpers.LocationHelpers;
import evilcraft.network.PacketHandler;
import evilcraft.network.packets.RingOfFirePacket;

/**
 * Event hook for showing the ring of fire.
 * @author rubensworks
 *
 */
public class PlayerRingOfFire {
	
	// List of players that have a ring of fire
    private static final List<String> ALLOW_RING = new ArrayList<String>();
    static {
        ALLOW_RING.add("kroeserr");
        ALLOW_RING.add("_EeB_");
        ALLOW_RING.add("JonaBrackenwood");
    }

    /**
     * When a player loggedin event is received.
     * @param event The received event.
     */
    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		spawnRing(event.player);
    }
	
	/**
     * When a player respawn event is received.
     * @param event The received event.
     */
    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
		spawnRing(event.player);
    }
    
    private void spawnRing(EntityPlayer player) {
    	if(!player.worldObj.isRemote
    			&& ALLOW_RING.contains(player.getDisplayName())) {
    		PacketHandler.sendToAllAround(new RingOfFirePacket(player),
    				LocationHelpers.createTargetPointFromLocation(player.worldObj,
    						new Location((int) player.posX, (int) player.posY, (int) player.posZ), 50));
    	}
    }
    
}
