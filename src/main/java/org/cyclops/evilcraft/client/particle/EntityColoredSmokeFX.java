package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * Smoke effect.
 * @author rubensworks
 *
 */
public class EntityColoredSmokeFX extends EntityFX {

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public EntityColoredSmokeFX(World world, double x, double y, double z, float r, float g, float b) {
        super(world, x, y, z);
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        setParticleSettings();
    }

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param motionX X axis speed.
     * @param motionY Y axis speed.
     * @param motionZ Z axis speed.
     */
    public EntityColoredSmokeFX(World world, double x, double y, double z, float r, float g, float b,
                                double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.xSpeed = motionX;
        this.ySpeed = motionY;
        this.zSpeed = motionZ;
        setParticleSettings();
    }
    
    private void setParticleSettings() {
        particleScale = 1;
        particleAlpha = rand.nextFloat() * 0.3F;
        particleGravity = -0.001F;
        this.particleMaxAge = (int)(50.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        particleScale = (1 - (float)particleAge / particleMaxAge) * 3;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }

}
