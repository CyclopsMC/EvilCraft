package evilcraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * Orbiting dark smoke effect.
 * @author rubensworks
 *
 */
public class EntityDarkSmokeFX extends EntityFX {
    
    /**
     * Make a new instance.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public EntityDarkSmokeFX(World world, double x, double y, double z) {
        super(world, x, y, z);
        setParticleSettings();
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
    public EntityDarkSmokeFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        setParticleSettings();
    }
    
    private void setParticleSettings() {
        particleScale = 1;
        particleAlpha = rand.nextFloat() * 0.3F;
        
        particleRed = rand.nextFloat() * 0.05F + 0.1F;
        particleGreen = rand.nextFloat() * 0.05F;
        particleBlue = rand.nextFloat() * 0.05F + 0.1F;
        
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

    /**
     * Set the visual living time.
     * @param livingFraction The fraction of aliveness (dead=0, newly spawned=1)
     */
	public void setLiving(float livingFraction) {
		// Temporarily ignore, fully opaque doesn't look pretty
		/*particleAlpha += livingFraction;
		if(particleAlpha > 1) particleAlpha = 1;*/
	}

	/**
	 * If the particles for this should be shown as death particles.
	 */
	public void setDeathParticles() {
		motionX *= 2;
        motionY *= 2;
        motionZ *= 2;
        particleGravity = 0.5F;
        
        particleRed = rand.nextFloat() * 0.33125F;
        particleGreen = rand.nextFloat() * 0.022187F;
        particleBlue = rand.nextFloat() * 0.3945F;
	}

}
