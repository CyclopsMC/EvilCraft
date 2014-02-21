package evilcraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
		super(writeStringToByteBuf(Unpooled.buffer(), "WHATTHEFLIP"), Reference.MOD_CHANNEL);
	}
	
	/**
	 * Creates a packet which contains the username of
	 * the player who spawned a fart.
	 * 
	 * @param username Name of the player who spawned a fart.
	 */
	public FartPacket(String username) {
		super(writeStringToByteBuf(Unpooled.buffer(), username), Reference.MOD_CHANNEL);
	}
	
	private static ByteBuf writeStringToByteBuf(ByteBuf buf, String s) {
		ByteBufUtils.writeUTF8String(buf, s);
		
		return buf;
	}
}
