package evilcraft.network.packet;

import evilcraft.Achievements;
import evilcraft.GeneralConfig;
import evilcraft.client.particle.EntityFartFX;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import evilcraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
    private static final List<UUID> ALLOW_RAINBOW_FARTS = new ArrayList<UUID>();
    static {
        ALLOW_RAINBOW_FARTS.add(UUID.fromString("068d4de0-3a75-4c6a-9f01-8c37e16a394c")); // kroeserr
        ALLOW_RAINBOW_FARTS.add(UUID.fromString("e1dc75c6-dcf9-4e0c-8fbf-9c6e5e44527c")); // _EeB_
        ALLOW_RAINBOW_FARTS.add(UUID.fromString("777e7aa3-9373-4511-8d75-f99d23ebe252")); // Davivs69
    }
	
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
		this.displayName = player.getDisplayName().getFormattedText();
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
        return player.getGameProfile() != null
                && ALLOW_RAINBOW_FARTS.contains(player.getGameProfile().getId());
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
