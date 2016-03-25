package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * A fart special effect.
 * @author immortaleeb
 *
 */
public class EntityFartFX extends EntityFX {
    
    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param rainbow If it should have rainbow colors.
     */
    public EntityFartFX(World world, double x, double y, double z, boolean rainbow) {
        super(world, x, y, z);
        
        setParticleSettings(rainbow);
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
     * @param rainbow If it should have rainbow colors.
     */
    public EntityFartFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean rainbow) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        
        setParticleSettings(rainbow);
    }
    
    private void setParticleSettings(boolean rainbow) {
        particleScale = 3;
        particleAlpha = 0.7F;
        
        if (!rainbow) {
            particleRed = 0.50F + rand.nextFloat() * 0.2F;
            particleGreen = 0.3F + rand.nextFloat() * 0.1F;
            particleBlue = rand.nextFloat() * 0.2F;
        } else {
            particleRed = rand.nextFloat();
            particleGreen = rand.nextFloat();
            particleBlue = rand.nextFloat();
        }
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
