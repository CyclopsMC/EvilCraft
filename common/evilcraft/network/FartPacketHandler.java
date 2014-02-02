package evilcraft.network;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.render.particle.EntityFartFX;

public class FartPacketHandler implements IPacketHandler {
    
    private static final int FART_RANGE = 64;
    private static final int MAX_PARTICLES = 200;
    private static final int MIN_PARTICLES = 100;
    
    private static final float PLAYER_Y_OFFSET = -0.8f;

    // List of players that have rainbow farts
    private static final String[] ALLOW_RAINBOW_FARTS = { "kroeserr", "_EeB_" };
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (player instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)player;
            
            if (Helpers.isClientSide())
                spawnFartParticles(entityPlayer);
            else // Note: we can get away with sending an empty packet because we only need the player data
                PacketDispatcher.sendPacketToAllAround(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, FART_RANGE, entityPlayer.dimension, PacketDispatcher.getPacket(Reference.MOD_CHANNEL, null));
        }
    }
    
    // south z+ 0
    // west x- 90
    // nort z- 180
    // east x+ 270
    
    @SideOnly(Side.CLIENT)
    private void spawnFartParticles(EntityPlayer player) {
        Random rand = player.worldObj.rand;
        int numParticles = rand.nextInt(MAX_PARTICLES - MIN_PARTICLES) + MIN_PARTICLES;
        boolean rainbow = hasRainbowFart(player);
        
        // Make corrections for the player rotation
        double yaw = (player.rotationYaw * Math.PI) / 180;
        double playerXOffset = Math.sin(yaw) * 0.7;
        double playerZOffset = -Math.cos(yaw) * 0.7;
        
        for (int i=0; i < numParticles; i++) {
            double extraDistance = rand.nextFloat() % 0.3;
            
            double particleX = player.posX + playerXOffset + extraDistance;
            double particleY = player.posY + PLAYER_Y_OFFSET;
            double particleZ = player.posZ + playerZOffset + extraDistance;
            
            float particleMotionX = -0.5F + rand.nextFloat();
            float particleMotionY = -0.5F + rand.nextFloat();
            float particleMotionZ = -0.5F + rand.nextFloat();
            
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFartFX(player.worldObj, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ, rainbow));
        }
    }
    
    public boolean hasRainbowFart(EntityPlayer player) {
        for (String name : ALLOW_RAINBOW_FARTS) {
            if (name.equals(player.username))
                return true;
        }
        
        return false;
    }
}
