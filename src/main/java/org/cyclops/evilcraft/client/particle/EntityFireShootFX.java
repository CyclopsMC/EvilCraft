package org.cyclops.evilcraft.client.particle;

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
     * @param xSpeed X axis speed.
     * @param ySpeed Y axis speed.
     * @param zSpeed Z axis speed.
     * @param scale Scale of the particle.
     */
    public EntityFireShootFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float scale) {
        super(world, x, y, z);
        
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        
        particleAlpha = 0.9F;
    }

}
