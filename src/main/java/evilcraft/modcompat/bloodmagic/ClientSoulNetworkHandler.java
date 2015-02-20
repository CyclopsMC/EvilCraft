package evilcraft.modcompat.bloodmagic;

import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.network.PacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;

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
	 * @param owner The owner.
	 * @return The essence.
	 */
	public int getCurrentEssence(String owner) {
		if(MinecraftHelpers.isClientSide()) {
			Integer ret = PLAYER_CACHE.get(owner);
			if(ret == null) {
				PacketHandler.sendToServer(new RequestSoulNetworkUpdatesPacket(owner));
				return 0;
			}
			return ret;
		} else {
			int essence = SoulNetworkHandler.getCurrentEssence(owner);
			return essence;
		}
	}
	
	/**
	 * Set the essence for the player.
	 * @param owner The player.
	 * @param currentEssence The essence.
	 */
	public void setCurrentEssence(String owner, int currentEssence) {
		PLAYER_CACHE.put(owner, currentEssence);
	}
	
	/**
	 * Add the given player to the server list of essence watchers.
	 * @param player Player
	 */
	//@SideOnly(Side.SERVER)
	public void addUpdatePlayer(String player) {
		UPDATE_PLAYERS.add(player);
	}
	
	/**
     * When a server tick event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onServerTick(ServerTickEvent event) {
        if(event.phase == Phase.START && WorldHelpers.efficientTick(MinecraftServer.getServer().worldServers[0], BoundBloodDropConfig.maxUpdateTicks)) {
        	Map<String, Integer> toSend = Maps.newHashMap();
        	for(String player : UPDATE_PLAYERS) {
        		int essence = SoulNetworkHandler.getCurrentEssence(player);
        		Integer found = PLAYER_CACHE.get(player);
        		if(found == null || essence != found) {
        			toSend.put(player, essence);
        			setCurrentEssence(player, essence);
        		}
        	}
        	sendUpdates(toSend);
        }
    }
    
    private void sendUpdates(Map<String, Integer> toSend) {
    	PacketHandler.sendToAll(new UpdateSoulNetworkCachePacket(toSend));
	}
	
    /**
     * When a world is loaded.
     * @param event the event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldEvent.Load event) {
        if(event.world.isRemote) {
        	reset();
        }
    }
	
}
