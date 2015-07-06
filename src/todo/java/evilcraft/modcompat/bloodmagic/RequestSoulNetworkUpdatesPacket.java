package evilcraft.modcompat.bloodmagic;

import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Packet from client to server to register a player for soul network updates.
 * 
 * @author rubensworks
 *
 */
public class RequestSoulNetworkUpdatesPacket extends PacketCodec {
	
    @CodecField
	private String player;

	/**
	 * Creates a packet with no content
	 */
	public RequestSoulNetworkUpdatesPacket() {
		
	}
	
	/**
	 * Creates a packet which contains the player name.
	 * @param player The player name.
	 */
	public RequestSoulNetworkUpdatesPacket(String player) {
		this.player = player;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		// Do nothing
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		ClientSoulNetworkHandler.getInstance().addUpdatePlayer(this.player);
	}
}
