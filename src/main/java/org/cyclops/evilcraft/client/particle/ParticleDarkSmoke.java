package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Orbiting dark smoke effect.
 * @author rubensworks
 * @see SmokeParticle
 */
public class ParticleDarkSmoke extends SpriteTexturedParticle {

    public ParticleDarkSmoke(World world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean entityDead) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        particleScale = 1;
        particleAlpha = rand.nextFloat() * 0.3F;

        particleRed = rand.nextFloat() * 0.05F + 0.1F;
        particleGreen = rand.nextFloat() * 0.05F;
        particleBlue = rand.nextFloat() * 0.05F + 0.1F;

        particleGravity = -0.001F;

        this.maxAge = (int)(50.0F / (this.rand.nextFloat() * 0.9F + 0.1F));

        if (entityDead) {
            setDeathParticles();
        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getScale(float p_217561_1_) {
        return this.particleScale * MathHelper.clamp(((float)this.age + p_217561_1_) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.motionY += 0.004D;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= (double)0.96F;
            this.motionY *= (double)0.96F;
            this.motionZ *= (double)0.96F;
            if (this.onGround) {
                this.motionX *= (double)0.7F;
                this.motionZ *= (double)0.7F;
            }

        }

        particleScale = (1 - (float)age / maxAge) * 3;
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
