package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.RainParticle;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

import javax.annotation.Nullable;
import java.util.Random;


/**
 * A blood bubble FX.
 * @author rubensworks
 * @see SplashParticle
 */
@OnlyIn(Dist.CLIENT)
public class ParticleBloodBubble extends RainParticle {

    public ParticleBloodBubble(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleGravity = 0.04F;
        if (ySpeedIn == 0.0D && (xSpeedIn != 0.0D || zSpeedIn != 0.0D)) {
            this.motionX = xSpeedIn;
            this.motionY = 0.1D;
            this.motionZ = zSpeedIn;
        }
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.002D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;
        if (this.maxAge-- <= 0) {
            this.setExpired();
        }
    }

    /**
     * Call this in machines that should display blood particles when working.
     * @param tile The tile working entity.
     * @param world The world.
     * @param blockPos The blockState position.
     * @param random Random instance.
     * @param rotatedDirection The direction to emit the particles at.
     */
    @OnlyIn(Dist.CLIENT)
    public static void randomDisplayTick(@Nullable WorkingTileEntity tile, World world, BlockPos blockPos, Random random, Direction rotatedDirection) {
        if(tile != null && random.nextInt(10) == 0) {
            if (tile.isVisuallyWorking()) {
                for(int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = blockPos.getX() - rotatedDirection.getXOffset() + (rotatedDirection == Direction.EAST ? 1 : 0)
                            + (rotatedDirection == Direction.NORTH || rotatedDirection == Direction.SOUTH ?
                            (0.3 + random.nextDouble() * 0.4) : 0);
                    double particleY = blockPos.getY() + 0.1 + random.nextDouble() * 0.5;
                    double particleZ = blockPos.getZ() - rotatedDirection.getZOffset() + (rotatedDirection == Direction.SOUTH ? 1 : 0)
                            + (rotatedDirection == Direction.EAST || rotatedDirection == Direction.WEST ?
                            (0.3 + random.nextDouble() * 0.4) : 0);

                    float particlemotionX = -0.1F + random.nextFloat() * 0.2F;
                    float particlemotionY = 0.01F;
                    float particlemotionZ = -0.1F + random.nextFloat() * 0.2F;

                    Minecraft.getInstance().worldRenderer.addParticle(
                            RegistryEntries.PARTICLE_BLOOD_BUBBLE, false,
                            particleX, particleY, particleZ,
                            particlemotionX, particlemotionY, particlemotionZ);
                }
            }
        }
    }
}
