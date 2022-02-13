package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.RainParticle;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Random;


/**
 * A blood splashing FX.
 * @author rubensworks
 * @see SplashParticle
 */
@OnlyIn(Dist.CLIENT)
public class ParticleBloodSplash extends RainParticle {

    public ParticleBloodSplash(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravity = 0.04F;
        if (ySpeedIn == 0.0D && (xSpeedIn != 0.0D || zSpeedIn != 0.0D)) {
            this.xd = xSpeedIn;
            this.yd = 0.1D;
            this.zd = zSpeedIn;
        }
    }

    public static void spawnParticles(World world, BlockPos blockPos, int velocity, int amount) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            float x_r = blockPos.getX() + random.nextFloat();
            float y_r = blockPos.getY() + random.nextFloat();
            float z_r = blockPos.getZ() + random.nextFloat();

            Minecraft.getInstance().levelRenderer.addParticle(
                    RegistryEntries.PARTICLE_BLOOD_SPLASH, false,
                    x_r, y_r, z_r,
                    velocity == 0 ? 0 : random.nextInt(velocity),
                    velocity == 0 ? 0 : random.nextInt(velocity),
                    velocity == 0 ? 0 : random.nextInt(velocity));
        }
    }
}
