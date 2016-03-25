package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.world.World;

/**
 * An extended {@link EntityExplodeFX}
 * @author rubensworks
 *
 */
public class ExtendedEntityExplodeFX extends EntityExplodeFX {

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param xSpeed The X motion speed.
	 * @param ySpeed The Y motion speed.
	 * @param zSpeed The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param alpha The particle alpha.
	 */
	public ExtendedEntityExplodeFX(World world, double x, double y, double z,
								   double xSpeed, double ySpeed, double zSpeed,
								   float red, float green, float blue, float alpha) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleAlpha = alpha;
	}

}
