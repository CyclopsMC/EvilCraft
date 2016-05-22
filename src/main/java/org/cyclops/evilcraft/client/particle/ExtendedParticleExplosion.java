package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.world.World;

/**
 * An extended {@link ParticleExplosion}
 * @author rubensworks
 *
 */
public class ExtendedParticleExplosion extends ParticleExplosion {

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param motionX The X motion speed.
	 * @param motionY The Y motion speed.
	 * @param motionZ The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param alpha The particle alpha.
	 */
	public ExtendedParticleExplosion(World world, double x, double y, double z,
									 double motionX, double motionY, double motionZ,
									 float red, float green, float blue, float alpha) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleAlpha = alpha;
	}

}
