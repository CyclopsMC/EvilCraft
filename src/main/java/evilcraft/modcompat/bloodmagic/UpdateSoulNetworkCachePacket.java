package evilcraft.modcompat.bloodmagic;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;

/**
 * Update the soul network cache at the clients originating from the server.
 * 
 * @author rubensworks
 *
 */
public class UpdateSoulNetworkCachePacket extends PacketCodec {
	
	@CodecField
	private Map<String, Integer> playerEssences = Maps.newHashMap();

	/**
	 * Creates a packet with no content
	 */
	public UpdateSoulNetworkCachePacket() {
		
	}
	
	/**
	 * Creates a packet which contains the player names and amount of essence.
	 * @param playerEssences A map of players with their essence.
	 */
	public UpdateSoulNetworkCachePacket(Map<String, Integer> playerEssences) {
		this.playerEssences = playerEssences;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		for(Map.Entry<String, Integer> entry : playerEssences.entrySet()) {
			ClientSoulNetworkHandler.getInstance().setCurrentEssence(entry.getKey(), entry.getValue());
		}
	}    

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		// Do nothing
	}
}
