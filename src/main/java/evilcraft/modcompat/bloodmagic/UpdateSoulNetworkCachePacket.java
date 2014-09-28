package evilcraft.modcompat.bloodmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
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
	private String player;
    @CodecField
	private int essence;

	/**
	 * Creates a packet with no content
	 */
	public UpdateSoulNetworkCachePacket() {
		
	}
	
	/**
	 * Creates a packet which contains the player name and amount of essence.
	 * @param player The data name.
	 * @param essence The amount of essence to update to.
	 */
	public UpdateSoulNetworkCachePacket(String player, int essence) {
		this.player = player;
		this.essence = essence;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		ClientSoulNetworkHandler.getInstance().setCurrentEssence(this.player, this.essence);
	}    

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		// Do nothing
	}
}
