package evilcraft.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.algorithms.ILocation;
import evilcraft.core.helpers.LocationHelpers;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import evilcraft.network.PacketHandler;
import evilcraft.render.particle.EntityBloodBrickFX;

/**
 * Packet for telling clients if a structure has been formed for a block location.
 * @author rubensworks
 *
 */
public class DetectionListenerPacket extends PacketCodec {

	private static final int RANGE = 100;
	
	@CodecField
	private int x = 0;
    @CodecField
	private int y = 0;
    @CodecField
	private int z = 0;
    @CodecField
	private boolean activation = true;
    
    /**
     * Empty packet.
     */
    public DetectionListenerPacket() {
    	
    }
    
    /**
	 * Creates a packet with coordinates.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param activation If the structure is being activated, otherwise deactivated.
	 */
	public DetectionListenerPacket(int x, int y, int z, boolean activation) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.activation = activation;
	}
	
	/**
	 * Creates a packet which contains the location data.
	 * @param location The location data.
	 * @param activation If the structure is being activated, otherwise deactivated.
	 */
	public DetectionListenerPacket(ILocation location, boolean activation) {
		this.x = location.getCoordinates()[0];
		this.y = location.getCoordinates()[1];
		this.z = location.getCoordinates()[2];
		this.activation = activation;
	}
    
    @SideOnly(Side.CLIENT)
    private void showActivatedParticle(World world, int x, int y, int z, ForgeDirection side) {
        EntityBloodBrickFX burst = new EntityBloodBrickFX(world, x, y, z, side);
		Minecraft.getMinecraft().effectRenderer.addEffect(burst);
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		if(activation) {
			for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				showActivatedParticle(world, x, y, z, side);
			}
		}
	}
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		PacketHandler.sendToAllAround(new DetectionListenerPacket(x, y, z, activation),
				LocationHelpers.createTargetPointFromEntityPosition(player, RANGE));
	}
	
}