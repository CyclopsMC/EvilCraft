package evilcraft.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The base packet for packets.
 * @author rubensworks
 *
 */
public abstract class PacketBase {
	
	/**
	 * Encode this packet.
	 * @param output The byte array to encode to.
	 */
	public abstract void encode(ByteArrayDataOutput output);

	/**
	 * Decode for this packet.
	 * @param input The byte array to decode from.
	 */
    public abstract void decode(ByteArrayDataInput input);

	/**
	 * Actions for client-side.
	 * @param world The world.
	 * @param player The player.
	 */
	@SideOnly(Side.CLIENT)
    public abstract void actionClient(World world, EntityPlayer player);

	/**
	 * Actions for server-side.
	 * @param world The world.
	 * @param player The player.
	 */
    public abstract void actionServer(World world, EntityPlayerMP player);
	
}
