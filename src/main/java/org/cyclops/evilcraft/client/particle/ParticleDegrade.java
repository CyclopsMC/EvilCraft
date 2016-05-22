package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

/**
 * An effect for magical particles.
 * @author rubensworks
 *
 */
public class ParticleDegrade extends Particle {
    
    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public ParticleDegrade(World world, double x, double y, double z) {
        super(world, x, y, z);
        setColor();
    }

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param motionX X axis speed.
     * @param motionY Y axis speed.
     * @param motionZ Z axis speed.
     */
    public ParticleDegrade(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
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
        
        this.motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
        this.motionY = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        this.motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
        
        particleScale = (1 - (float)particleAge / particleMaxAge) * 0.9F;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }

}
