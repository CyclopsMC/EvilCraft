package org.cyclops.evilcraft.modcompat.bloodmagic;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Map;
import java.util.Set;

/**
 * A client-side cache for the soul network contents.
 * @author rubensworks
 */
public class ClientSoulNetworkHandler {

	private static ClientSoulNetworkHandler _instance = null;
	private Map<String, Integer> PLAYER_CACHE = Maps.newHashMap();
	private Set<String> UPDATE_PLAYERS = Sets.newHashSet();
	
	private ClientSoulNetworkHandler() {
		
	}
	
	/**
	 * Reset the instance.
	 */
	public static void reset() {
		getInstance().PLAYER_CACHE = Maps.newHashMap();
	}
	
	/**
	 * @return The unique instance.
	 */
	public static ClientSoulNetworkHandler getInstance() {
		if(_instance == null) {
			_instance = new ClientSoulNetworkHandler();
		}
		return _instance;
	}
	
	/**
	 * Get the cached current essence.
	 * Clients will automatically send a request packet to the server to stay updated for this player's essence.
	 * Servers will always delegate to the SoulNetworkHandler.
	 * @param uuid The owner uuid.
	 * @return The essence.
	 */
	public int getCurrentEssence(String uuid) {
		if(MinecraftHelpers.isClientSide()) {
			Integer ret = PLAYER_CACHE.get(uuid);
			if(ret == null) {
				EvilCraft._instance.getPacketHandler().sendToServer(new RequestSoulNetworkUpdatesPacket(uuid));
				return 0;
			}
			return ret;
		} else {
			return NetworkHelper.getSoulNetwork(uuid).getCurrentEssence();
		}
	}
	
	/**
	 * Set the essence for the player.
	 * @param uuid The player uuid.
	 * @param currentEssence The essence.
	 */
	public void setCurrentEssence(String uuid, int currentEssence) {
		PLAYER_CACHE.put(uuid, currentEssence);
	}
	
	/**
	 * Add the given player to the server list of essence watchers.
	 * @param uuid Player uuid
	 */
	//@SideOnly(Side.SERVER)
	public void addUpdatePlayer(String uuid) {
		UPDATE_PLAYERS.add(uuid);
	}
	
	/**
     * When a server tick event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onServerTick(ServerTickEvent event) {
        if(event.phase == Phase.START && WorldHelpers.efficientTick(
				FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0],
				BoundBloodDropConfig.maxUpdateTicks)) {
        	Map<String, Integer> toSend = Maps.newHashMap();
        	for(String uuid : UPDATE_PLAYERS) {
        		int essence = NetworkHelper.getSoulNetwork(uuid).getCurrentEssence();
        		Integer found = PLAYER_CACHE.get(uuid);
        		if(found == null || essence != found) {
        			toSend.put(uuid, essence);
        			setCurrentEssence(uuid, essence);
        		}
        	}
        	sendUpdates(toSend);
        }
    }
    
    private void sendUpdates(Map<String, Integer> toSend) {
    	EvilCraft._instance.getPacketHandler().sendToAll(new UpdateSoulNetworkCachePacket(toSend));
	}
	
    /**
     * When a world is loaded.
     * @param event the event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldEvent.Load event) {
        if(event.getWorld().isRemote) {
        	reset();
        }
    }
	
}
