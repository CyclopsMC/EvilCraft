package evilcraft.network.packets;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Achievements;
import evilcraft.GeneralConfig;
import evilcraft.api.helpers.LocationHelpers;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import evilcraft.network.PacketHandler;
import evilcraft.render.particle.EntityFartFX;

/**
 * Instances of this class are sent between client
 * and server to notify both about a farts which
 * are spawned.
 * 
 * @author immortaleeb
 *
 */
public class FartPacket extends PacketCodec {
	
	private static final int FART_RANGE = 3000;
    private static final int MAX_PARTICLES = 200;
    private static final int MIN_PARTICLES = 100;
    
    private static final float CLIENT_PLAYER_Y_OFFSET = -0.8f;
    private static final float REMOTE_PLAYER_Y_OFFSET = 0.65f;

    // List of players that have rainbow farts
    private static final String[] ALLOW_RAINBOW_FARTS = { "kroeserr", "_EeB_" };
	
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
	public FartPacket() {
		
	}
	
	/**
	 * Creates a FartPacket which contains the player data.
	 * @param player The player data.
	 */
	public FartPacket(EntityPlayer player) {
		this.displayName = player.getDisplayName();
		this.x = player.posX;
		this.y = player.posY;
		this.z = player.posZ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		if(GeneralConfig.farting) {
			boolean isRemotePlayer = !player.getDisplayName().equals(displayName);
	         
			if (isRemotePlayer) {
				player = world.getPlayerEntityByName(displayName);
				spawnFartParticles(world, player, x, y, z, true);
			} else {
				spawnFartParticles(world, player, false);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
    private void spawnFartParticles(World world, EntityPlayer player, boolean isRemotePlayer) {
        spawnFartParticles(world, player, player.posX, player.posY, player.posZ, isRemotePlayer);
    }
    
    @SideOnly(Side.CLIENT)
    private void spawnFartParticles(
            World world, EntityPlayer player, 
            double posX, double posY, double posZ, boolean isRemotePlayer) {
        
        if (player == null)
            return;
        
        Random rand = world.rand;
        int numParticles = rand.nextInt(MAX_PARTICLES - MIN_PARTICLES) + MIN_PARTICLES;
        boolean rainbow = hasRainbowFart(player);
        
        // Make corrections for the player rotation
        double yaw = (player.rotationYaw * Math.PI) / 180;
        double playerXOffset = Math.sin(yaw) * 0.7;
        double playerZOffset = -Math.cos(yaw) * 0.7;
        
        // Make corrections for the location of the player's bottom
        float playerYOffset = isRemotePlayer ? REMOTE_PLAYER_Y_OFFSET : CLIENT_PLAYER_Y_OFFSET;
        
        for (int i=0; i < numParticles; i++) {
            double extraDistance = rand.nextFloat() % 0.3;
            
            double particleX = posX + playerXOffset + extraDistance;
            double particleY = posY + playerYOffset;
            double particleZ = posZ + playerZOffset + extraDistance;
            
            float particleMotionX = -0.5F + rand.nextFloat();
            float particleMotionY = -0.5F + rand.nextFloat();
            float particleMotionZ = -0.5F + rand.nextFloat();
            
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFartFX(world, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ, rainbow));
        }
    }
    
    /**
     * Check if the given player should have rainbow farts.
     * @param player The player to check.
     * @return If that player has rainbow farts.
     */
    public boolean hasRainbowFart(EntityPlayer player) {
        for (String name : ALLOW_RAINBOW_FARTS) {
            if (name.equals(player.getCommandSenderName()))
                return true;
        }
        
        return false;
    }

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		if(GeneralConfig.farting) {
			player.addStat(Achievements.FART, 1);
			PacketHandler.sendToAllAround(new FartPacket(player),
					LocationHelpers.createTargetPointFromEntityPosition(player, FART_RANGE));
		}
	}
}
