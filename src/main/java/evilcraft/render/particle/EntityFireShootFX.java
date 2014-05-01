package evilcraft.render.particle;

import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.world.World;

/**
 * An effect for shooting flames in the sky.
 * @author rubensworks
 *
 */
public class EntityFireShootFX extends EntityLavaFX {

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param motionX X axis speed.
     * @param motionY Y axis speed.
     * @param motionZ Z axis speed.
     * @param scale Scale of the particle.
     */
    public EntityFireShootFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
        super(world, x, y, z);
        
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        
        particleAlpha = 0.9F;
        
        this.noClip = false;
    }

}
