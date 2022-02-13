package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.PoofParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

/**
 * An extended {@link PoofParticle}
 * @author rubensworks
 *
 */
public class ParticleExplosionExtended extends PoofParticle {

	public ParticleExplosionExtended(ClientWorld world, double x, double y, double z,
									 double motionX, double motionY, double motionZ,
									 float red, float green, float blue, float alpha,
									 IAnimatedSprite sprite) {
		super(world, x, y, z, motionX, motionY, motionZ, sprite);
		this.rCol = red;
		this.gCol = green;
		this.bCol = blue;
		this.alpha = alpha;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
