package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * An effect for magical particles.
 * @author rubensworks
 *
 */
public class EntityDegradeFX extends EntityFX {
    
    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public EntityDegradeFX(World world, double x, double y, double z) {
        super(world, x, y, z);
        setColor();
    }

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param xSpeed X axis speed.
     * @param ySpeed Y axis speed.
     * @param zSpeed Z axis speed.
     */
    public EntityDegradeFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.particleMaxAge = 40;
        this.particleAlpha = 0.4F;
        setColor();
    }
    
    private void setColor() {
        particleRed = 0.3F + rand.nextFloat() * 0.2F;
        particleGreen = 0.2F + rand.nextFloat() * 0.1F;
        particleBlue = 0.25F + rand.nextFloat() * 0.45F;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        this.xSpeed = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
        this.ySpeed = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        this.zSpeed = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
        
        particleScale = (1 - (float)particleAge / particleMaxAge) * 0.9F;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }

}
