package evilcraft.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import evilcraft.EvilCraft;

/**
 * This class handles key RemoteKeyS sent by the client.
 * Whenever a user presses one of the custom hotkeys client side,
 * the server is notified through the RemoteKeyHandler who receives
 * a RemoteKey containing the key pressed aswel as its current state.
 * 
 * Based on an elegant way to handle custom hotkeys server-side by
 * Nico Bergemann (BarracudaATA) in his "Dragon mounts" mod 
 * (http://www.minecraftforum.net/topic/827863-16x-dragon-mounts-r33-wip/)
 * 
 * @author immortaleeb
 *
 */
public class RemoteKeyHandler implements IPacketHandler {
	
	private static RemoteKeyHandler _instance = null;
	
	public static RemoteKeyHandler getInstance() {
		if (_instance == null)
			_instance = new RemoteKeyHandler();
		
		return _instance;
	}
	
	private RemoteKeyHandler() {}
	
	// Maps the player to the keys he pressed
	private Map<String, Map<String, Boolean>> keysPressed = new HashMap<String, Map<String, Boolean>>();

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			
			RemoteKey rk = RemoteKey.fromByteArray(packet.data);
			if (rk != null)
				setKeyPressed(playerMP.username, rk.name, rk.pressed);
		}
	}
	
	public boolean isKeyPressed(String username, String key) {
		Map<String, Boolean> map = keysPressed.get(username);
		if (map == null)
			return false;
		
		Boolean res = map.get(key);
		return (res != null) ? res.booleanValue() : false;
	}
	
	private void setKeyPressed(String player, String keyDescr, boolean pressed) {
		Map<String, Boolean> map = null;
		
		if (!keysPressed.containsKey(player)) {
			map = new HashMap<String, Boolean>();
			keysPressed.put(player, map);
		} else {
			map = keysPressed.get(player);
		}
		
		map.put(keyDescr, pressed);
	}
	
	public void clearKeyData(String username) {
		keysPressed.remove(username);
	}
	
	public void clearKeyData(String username, String[] keys) {
		Map<String, Boolean> map = keysPressed.get(username);
		if (map != null) {
			for (String key : keys)
				map.remove(key);
			
			if (map.isEmpty())
				keysPressed.remove(username);
		}
	}

}
