package org.cyclops.evilcraft.modcompat.bloodmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to register a player for soul network updates.
 * 
 * @author rubensworks
 *
 */
public class RequestSoulNetworkUpdatesPacket extends PacketCodec {
	
    @CodecField
	private String uuid;

	/**
	 * Creates a packet with no content
	 */
	public RequestSoulNetworkUpdatesPacket() {
		
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	/**
	 * Creates a packet which contains the player uuid.
	 * @param uuid The player uuid.
	 */
	public RequestSoulNetworkUpdatesPacket(String uuid) {
		this.uuid = uuid;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		// Do nothing
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		ClientSoulNetworkHandler.getInstance().addUpdatePlayer(this.uuid);
	}
}
