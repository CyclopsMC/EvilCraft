package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

import javax.annotation.Nullable;
import java.util.Random;


/**
 * A blood bubble FX.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleBloodBubble extends ParticleSplash {

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param speedX X axis speed.
     * @param speedY Y axis speed.
     * @param speedZ Z axis speed.
     */
    public ParticleBloodBubble(World world, double x, double y, double z, double speedX, double speedY, double speedZ) {
        super(world, x, y, z, speedX, speedY, speedZ);
        this.setParticleTextureIndex(32);
        this.setSize(0.01F, 0.01F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleRed = 1.0F;
        this.particleGreen = 0.0F;
        this.particleBlue = 0.0F;

        this.motionX = speedX;
        this.motionY = speedY + 0.1D;
        this.motionZ = speedZ;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.002D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;
        if (this.particleMaxAge-- <= 0) {
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
    @SideOnly(Side.CLIENT)
    public static void randomDisplayTick(@Nullable WorkingTileEntity tile, World world, BlockPos blockPos, Random random, EnumFacing rotatedDirection) {
        if(tile != null && random.nextInt(10) == 0) {
            if (tile.isVisuallyWorking()) {
                for(int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = blockPos.getX() - rotatedDirection.getXOffset() + (rotatedDirection == EnumFacing.EAST ? 1 : 0)
                            + (rotatedDirection == EnumFacing.NORTH || rotatedDirection == EnumFacing.SOUTH ?
                            (0.3 + random.nextDouble() * 0.4) : 0);
                    double particleY = blockPos.getY() + 0.1 + random.nextDouble() * 0.5;
                    double particleZ = blockPos.getZ() - rotatedDirection.getZOffset() + (rotatedDirection == EnumFacing.SOUTH ? 1 : 0)
                            + (rotatedDirection == EnumFacing.EAST || rotatedDirection == EnumFacing.WEST ?
                            (0.3 + random.nextDouble() * 0.4) : 0);

                    float particlemotionX = -0.1F + random.nextFloat() * 0.2F;
                    float particlemotionY = 0.01F;
                    float particlemotionZ = -0.1F + random.nextFloat() * 0.2F;

                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                            new ParticleBloodBubble(world, particleX, particleY, particleZ,
                                    particlemotionX, particlemotionY, particlemotionZ)
                    );
                }
            }
        }
    }
}
