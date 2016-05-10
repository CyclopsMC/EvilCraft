package org.cyclops.evilcraft.network.packet;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.particle.EntityBloodSplashFX;

/**
 * Packet for playing a sound at a location.
 * @author rubensworks
 *
 */
public class SanguinaryPedestalBlockReplacePacket extends PacketCodec {

	private static final int RANGE = 15;
	
	@CodecField
	private double x = 0;
    @CodecField
	private double y = 0;
    @CodecField
	private double z = 0;
    @CodecField
	private int blockID = 0;
    
    /**
     * Empty packet.
     */
    public SanguinaryPedestalBlockReplacePacket() {
    	
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	/**
	 * Creates a packet with coordinates.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param blockID The blockState ID.
	 */
	public SanguinaryPedestalBlockReplacePacket(double x, double y, double z, int blockID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockID = blockID;
	}
	
	/**
	 * Creates a packet with coordinates.
     * @param location The location data.
     * @param block The blockState.
	 */
	public SanguinaryPedestalBlockReplacePacket(BlockPos location, Block block) {
		this(location.getX(), location.getY(), location.getZ(), Block.getIdFromBlock(block));
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		Block block = Block.getBlockById(blockID);
		EvilCraft.proxy.playSound(x, y, z, block.getStepSound().getBreakSound(), SoundCategory.BLOCKS, 0.1F + world.rand.nextFloat() * 0.5F,
    			0.9F + world.rand.nextFloat() * 0.1F);
		EntityBloodSplashFX.spawnParticles(world, new BlockPos((int) x, (int) y + 1, (int) z), 3 + world.rand.nextInt(2), 1 + world.rand.nextInt(2));
	}
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		EvilCraft._instance.getPacketHandler().sendToAllAround(new SanguinaryPedestalBlockReplacePacket(x, y, z, blockID),
				LocationHelpers.createTargetPointFromEntityPosition(player, RANGE));
	}
	
}