package org.cyclops.evilcraft.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.packet.PlayerPositionPacket;
import org.cyclops.evilcraft.Advancements;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.client.particle.ParticleFartData;

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
public class FartPacket extends PlayerPositionPacket {

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

    /**
     * Creates a packet with no content
     */
    public FartPacket() {

    }

    /**
     * Creates a FartPacket which contains the player data.
     * @param player The player data.
     */
    public FartPacket(PlayerEntity player) {
        super(player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(World world, PlayerEntity player) {
        if(GeneralConfig.farting)
            super.actionClient(world, player);

    }

    @Override
    protected void performClientAction(World world, PlayerEntity player) {
        spawnFartParticles(world, player, position, true);
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnFartParticles(
            World world, PlayerEntity player,
            Vector3d pos, boolean isClientSidePlayer) {
        
        if (player == null)
            return;
        
        Random rand = world.random;
        int numParticles = rand.nextInt(MAX_PARTICLES - MIN_PARTICLES) + MIN_PARTICLES;
        boolean rainbow = hasRainbowFart(player);
        
        // Make corrections for the player rotation
        double yaw = (player.yRot * Math.PI) / 180;
        double playerXOffset = Math.sin(yaw) * 0.7;
        double playerZOffset = -Math.cos(yaw) * 0.7;
        
        // Make corrections for the location of the player's bottom
        float playerYOffset = isClientSidePlayer ? REMOTE_PLAYER_Y_OFFSET : CLIENT_PLAYER_Y_OFFSET;
        
        for (int i=0; i < numParticles; i++) {
            double extraDistance = rand.nextFloat() % 0.3;
            
            double particleX = pos.x + playerXOffset + extraDistance;
            double particleY = pos.y + playerYOffset;
            double particleZ = pos.z + playerZOffset + extraDistance;
            
            float particleMotionX = -0.5F + rand.nextFloat();
            float particleMotionY = -0.5F + rand.nextFloat();
            float particleMotionZ = -0.5F + rand.nextFloat();

            world.addParticle(new ParticleFartData(rainbow), particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }
    
    /**
     * Check if the given player should have rainbow farts.
     * @param player The player to check.
     * @return If that player has rainbow farts.
     */
    public boolean hasRainbowFart(PlayerEntity player) {
        return player.getGameProfile() != null
                && ALLOW_RAINBOW_FARTS.contains(player.getGameProfile().getId());
    }

    @Override
    public void actionServer(World world, ServerPlayerEntity player) {
        if(GeneralConfig.farting) {
            Advancements.FART.test(player);
            super.actionServer(world, player);
        }
    }

    @Override
    protected PlayerPositionPacket create(PlayerEntity player, int range) {
        return new FartPacket(player);
    }

    @Override
    protected ModBase getModInstance() {
        return EvilCraft._instance;
    }
}
