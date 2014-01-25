package evilcraft.render.particle;

import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * A blood splashing FX.
 */
@SideOnly(Side.CLIENT)
public class EntityBloodSplashFX extends EntitySplashFX {

    public EntityBloodSplashFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        this.particleRed = 1.0F;
        this.particleGreen = 0.0F;
        this.particleBlue = 0.0F;
    }
    
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
