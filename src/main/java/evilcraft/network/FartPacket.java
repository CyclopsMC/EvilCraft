package evilcraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import evilcraft.Reference;

/**
 * Instances of this class are sent between client
 * and server to notify both about a farts which
 * are spawned.
 * 
 * @author immortaleeb
 *
 */
public class FartPacket extends FMLProxyPacket {

	/**
	 * Creates a packet with no content
	 */
	public FartPacket() {
		// Ideally we would have an empty buffer here, but for some reason that doesn't work...
		super(Unpooled.buffer(), Reference.MOD_CHANNEL);
	}
	
	/**
	 * Creates a FartPacket which contains the contents
	 * of the given {@link ByteBuf}.
	 * 
	 * @param buffer
	 */
	public FartPacket(ByteBuf buffer) {
		super(buffer, Reference.MOD_CHANNEL);
	}
	
	/**
	 * Creates a new FartPacket which contains the username
	 * and position of the given player.
	 * 
	 * @param player The player who's position and name will be saved in the packet.
	 * @return Returns a FartPacket with the data of the supplied player.
	 */
	public static FartPacket createFartPacket(EntityPlayer player) {
		ByteBuf buffer = Unpooled.buffer();
		
		ByteBufUtils.writeUTF8String(buffer, player.getDisplayName());
		buffer.writeDouble(player.posX);
		buffer.writeDouble(player.posY);
		buffer.writeDouble(player.posZ);
		
		return new FartPacket(buffer);
	}
}
