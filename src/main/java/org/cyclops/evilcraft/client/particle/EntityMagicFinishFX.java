package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * An effect for magical particles.
 * @author rubensworks
 *
 */
public class EntityMagicFinishFX extends EntityFX {
    
    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public EntityMagicFinishFX(World world, double x, double y, double z) {
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
    public EntityMagicFinishFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        setColor();
    }
    
    private void setColor() {
        particleRed = 0.78F + rand.nextFloat() * 0.5F;
        particleGreen = 0.09F + rand.nextFloat() * 0.5F;
        particleBlue = 0.65F + rand.nextFloat() * 0.5F;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        particleScale = (1 - (float)particleAge / particleMaxAge) / 2;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }

}
