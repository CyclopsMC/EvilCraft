package evilcraft.client.particle;

import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * A blood splashing FX.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class EntityBloodSplashFX extends EntitySplashFX {

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
    public EntityBloodSplashFX(World world, double x, double y, double z, double speedX, double speedY, double speedZ) {
        super(world, x, y, z, speedX, speedY, speedZ);
        this.particleRed = 1.0F;
        this.particleGreen = 0.0F;
        this.particleBlue = 0.0F;
    }
    
    /**
     * Spawn particles.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param velocity The velocity of the particle.
     * @param amount The amount of particles to spawn.
     */
    public static void spawnParticles(World world, int x, int y, int z, int velocity, int amount) {
        Random random = new Random();
        for(int i = 0; i < amount; i++) {
            float x_r = x + random.nextFloat();
            float y_r = y + random.nextFloat();
            float z_r = z + random.nextFloat();
            EntityFX fx = new EntityBloodSplashFX(world, x_r, y_r, z_r, random.nextInt(velocity), random.nextInt(velocity), random.nextInt(velocity));
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }
}
