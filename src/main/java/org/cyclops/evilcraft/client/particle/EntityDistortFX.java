package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import org.cyclops.evilcraft.item.MaceOfDistortion;

/**
 * An effect for the distortion radial effect.
 * @author rubensworks
 *
 */
public class EntityDistortFX extends EntityFX {

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
    public EntityDistortFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
        super(world, x, y, z, motionX, motionY, motionZ);
        
        particleScale = scale;
        particleAlpha = 0.3F;
        particleMaxAge = MaceOfDistortion.AOE_TICK_UPDATE;
        
        particleRed = 1.0F * rand.nextFloat();
        particleGreen = 0.01F * rand.nextFloat();
        particleBlue = 0.5F * rand.nextFloat();
        
        this.noClip = false;
    }
    
    @Override
    public void onUpdate() {
        particleScale = (1 - (float)particleAge / particleMaxAge) * 3;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        super.onUpdate();
    }

}
