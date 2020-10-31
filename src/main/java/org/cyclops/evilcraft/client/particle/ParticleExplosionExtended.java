package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.PoofParticle;
import net.minecraft.world.World;

/**
 * An extended {@link PoofParticle}
 * @author rubensworks
 *
 */
public class ParticleExplosionExtended extends PoofParticle {

	public ParticleExplosionExtended(World world, double x, double y, double z,
									 double motionX, double motionY, double motionZ,
									 float red, float green, float blue, float alpha,
									 IAnimatedSprite sprite) {
		super(world, x, y, z, motionX, motionY, motionZ, sprite);
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleAlpha = alpha;
	}

}
