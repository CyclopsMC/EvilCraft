package evilcraft.network.packet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.particle.EntityFireShootFX;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import evilcraft.network.PacketHandler;

/**
 * Packet for sending and showing the ring of fire.
 * 
 * @author rubensworks
 *
 */
public class RingOfFirePacket extends PacketCodec {
	
	private static final int RANGE = 3000;

	// List of players that have a ring of fire
    private static final List<String> ALLOW_RING = new ArrayList<String>();
    static {
        ALLOW_RING.add("kroeserr");
        ALLOW_RING.add("_EeB_");
        ALLOW_RING.add("JonaBrackenwood");
        ALLOW_RING.add("Davivs69");
    }
    private static double RING_AREA = 0.9F;
	
    @CodecField
	private String displayName;
    @CodecField
	private double x = 0;
    @CodecField
	private double y = 0;
    @CodecField
	private double z = 0;

	/**
	 * Creates a packet with no content
	 */
	public RingOfFirePacket() {
		
	}
	
	/**
	 * Creates a FartPacket which contains the player data.
	 * @param player The player data.
	 */
	public RingOfFirePacket(EntityPlayer player) {
		this.displayName = player.getDisplayName();
		this.x = player.posX;
		this.y = player.posY;
		this.z = player.posZ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		if(!player.getDisplayName().equals(displayName)) {
			player = world.getPlayerEntityByName(displayName);
		}
		if(player != null) {
			showFireRing(player);
		}
	}
    
	@SideOnly(Side.CLIENT)
    private static void showFireRing(Entity entity) {
        double area = RING_AREA;
        World world = entity.worldObj;
        int points = 40;
        for(double point = -points; point <= points; point++) {
            double u = 2 * Math.PI * (point / points);

            double xOffset = Math.cos(u) * area;
            double yOffset = 0F;
            double zOffset = Math.sin(u) * area;

            double xCoord = entity.posX;
            double yCoord = entity.posY - 1;
            double zCoord = entity.posZ;

            double particleX = xCoord + xOffset + world.rand.nextFloat() / 5;
            double particleY = yCoord + yOffset + world.rand.nextFloat() / 5;
            double particleZ = zCoord + zOffset + world.rand.nextFloat() / 5;

            float particleMotionX = (float)xOffset / 5;
            float particleMotionY = 0.25F;
            float particleMotionZ = (float)zOffset / 5;

            if(world.rand.nextInt(20) == 0) {
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityFireShootFX(world, particleX, particleY, particleZ,
                                particleMotionX, particleMotionY, particleMotionZ, 0.1F)
                        );
            } else {
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityFlameFX(world, particleX, particleY, particleZ, 0, 0, 0)
                        );
            }
        }
    }

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		PacketHandler.sendToAllAround(new RingOfFirePacket(player),
				LocationHelpers.createTargetPointFromEntityPosition(player, RANGE));
	}
}
