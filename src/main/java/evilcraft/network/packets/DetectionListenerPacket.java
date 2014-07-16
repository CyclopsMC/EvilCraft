package evilcraft.network.packets;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.algorithms.ILocation;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import evilcraft.network.PacketHandler;
import evilcraft.render.particle.EntityBlurFX;

/**
 * Packet for telling clients if a structure has been formed for a block location.
 * @author rubensworks
 *
 */
public class DetectionListenerPacket extends PacketCodec {

	private static final int RANGE = 100;
	private static final int PARTICLE_COUNT = 10;
	
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
    private void showActivatedParticle(World world, int x, int y, int z) {
    	Random rand = world.rand;
    	float posX = x + rand.nextFloat();
    	float posY = y + rand.nextFloat();
    	float posZ = z + rand.nextFloat();
    	float motionX = rand.nextFloat() * 0.1F - 0.05F;
    	float motionY = rand.nextFloat() * 0.1F - 0.05F;
    	float motionZ = rand.nextFloat() * 0.1F - 0.05F;
    	float scale = 0.1F + rand.nextFloat() * 0.1F;
    	float red, green, blue;
    	if(activation) {
	    	red = rand.nextFloat() * 0.4F + 0.4F;
	        green = rand.nextFloat() * 0.05F;
	        blue = rand.nextFloat() * 0.05F;
    	} else {
    		red = rand.nextFloat() * 0.1F;
	        green = rand.nextFloat() * 0.1F;
	        blue = rand.nextFloat() * 0.1F;
    	}
        float ageMultiplier = (float) (rand.nextDouble() * 5D + 20D); 
        
		EntityBlurFX square = new EntityBlurFX(world, posX, posY, posZ, scale, motionX, motionY, motionZ,
				red, green, blue, ageMultiplier);
		Minecraft.getMinecraft().effectRenderer.addEffect(square);
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		for(int i = 0; i < PARTICLE_COUNT; i++) {
			showActivatedParticle(world, x, y, z);
		}
	}
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		PacketHandler.sendToAllAround(new DetectionListenerPacket(x, y, z, activation),
				Helpers.createTargetPointFromEntityPosition(player, RANGE));
	}
	
}