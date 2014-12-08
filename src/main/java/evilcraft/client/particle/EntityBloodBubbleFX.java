package evilcraft.client.particle;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.tileentity.WorkingTileEntity;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import javax.annotation.Nullable;
import java.util.Random;


/**
 * A blood bubble FX.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class EntityBloodBubbleFX extends EntitySplashFX {

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
    public EntityBloodBubbleFX(World world, double x, double y, double z, double speedX, double speedY, double speedZ) {
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
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }

    /**
     * Call this in machines that should display blood particles when working.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @param random Random instance.
     */
    @SideOnly(Side.CLIENT)
    public static void randomDisplayTick(@Nullable WorkingTileEntity tile, World world, int x, int y, int z, Random random) {
        if(tile != null && random.nextInt(10) == 0) {
            ForgeDirection rotatedDirection = tile.getRotation();
            if (tile.isVisuallyWorking()) {
                for(int i = 0; i < 1 + random.nextInt(5); i++) {
                    double particleX = x - rotatedDirection.offsetX + (rotatedDirection == ForgeDirection.EAST ? 1 : 0)
                            + (rotatedDirection == ForgeDirection.NORTH || rotatedDirection == ForgeDirection.SOUTH ?
                            (0.3 + random.nextDouble() * 0.4) : 0);
                    double particleY = y + 0.1 + random.nextDouble() * 0.5;
                    double particleZ = z - rotatedDirection.offsetZ + (rotatedDirection == ForgeDirection.SOUTH ? 1 : 0)
                            + (rotatedDirection == ForgeDirection.EAST || rotatedDirection == ForgeDirection.WEST ?
                            (0.3 + random.nextDouble() * 0.4) : 0);

                    float particleMotionX = -0.1F + random.nextFloat() * 0.2F;
                    float particleMotionY = 0.01F;
                    float particleMotionZ = -0.1F + random.nextFloat() * 0.2F;

                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                            new EntityBloodBubbleFX(world, particleX, particleY, particleZ,
                                    particleMotionX, particleMotionY, particleMotionZ)
                    );
                }
            }
        }
    }
}
