package evilcraft.network;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.render.particle.EntityFartFX;

/**
 * An {@link IPacketHandler} for sending farts.
 * @author immortaleeb
 *
 */
public class FartPacketHandler implements IPacketHandler {
    
    private static final int FART_RANGE = 64;
    private static final int MAX_PARTICLES = 200;
    private static final int MIN_PARTICLES = 100;
    
    private static final float CLIENT_PLAYER_Y_OFFSET = -0.8f;
    private static final float REMOTE_PLAYER_Y_OFFSET = 0.65f;

    // List of players that have rainbow farts
    private static final String[] ALLOW_RAINBOW_FARTS = { "kroeserr", "_EeB_" };
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (player instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)player;
            
            if (Helpers.isClientSide() && packet.data != null) {
                handlePacketData(entityPlayer, packet.data);
                
            } else if (!Helpers.isClientSide()){
                
                PacketDispatcher.sendPacketToAllAround(
                        entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, FART_RANGE, 
                        entityPlayer.dimension, createPacket(entityPlayer)
                );
            }
        }
    }
    
    private Packet250CustomPayload createPacket(EntityPlayer entityPlayer) {
        byte[] usernameBytes = entityPlayer.username.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(4 + usernameBytes.length + 3 * 8);
        
        buffer.putInt(usernameBytes.length);
        buffer.put(usernameBytes);
        
        /**
         * Due to some unknown reason, using the position of an EntityPlayer sometimes displays
         * farts in the wrong place, so we have to send along the position of the player from
         * the server to the client so farts are displayed on the correct positions
         */
        buffer.putDouble(entityPlayer.posX);
        buffer.putDouble(entityPlayer.posY);
        buffer.putDouble(entityPlayer.posZ);
        
        return PacketDispatcher.getPacket(Reference.MOD_CHANNEL, buffer.array());
    }
    
    private void handlePacketData(EntityPlayer entityPlayer, byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        
        World world = entityPlayer.worldObj;
        
        try {
            // Get the username of the player
            int usernameLength = buffer.getInt();
            byte[] usernameBytes = new byte[usernameLength];
            buffer.get(usernameBytes);
            String username = new String(usernameBytes);
            
            boolean isRemotePlayer = !entityPlayer.username.equals(username);
            
            if (isRemotePlayer) {
                entityPlayer = world.getPlayerEntityByName(username);
                
                // Get the player's position
                double posX = buffer.getDouble();
                double posY = buffer.getDouble();
                double posZ = buffer.getDouble();
                
                spawnFartParticles(world, entityPlayer, posX, posY, posZ, true);
            } else {
                spawnFartParticles(world, entityPlayer, false);
            }
        } catch (BufferUnderflowException e) {
            EvilCraft.log("Failed to parse fart packet: " + e.getMessage(), Level.SEVERE);
        }
    }
    
    // south z+ 0
    // west x- 90
    // nort z- 180
    // east x+ 270
    
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
            if (name.equals(player.username))
                return true;
        }
        
        return false;
    }
}
