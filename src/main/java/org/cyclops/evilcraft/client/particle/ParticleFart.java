package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * A fart special effect.
 * @author immortaleeb
 *
 */
public class ParticleFart extends SpriteTexturedParticle {

    public ParticleFart(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean rainbow) {
        super(world, x, y, z, motionX, motionY, motionZ);

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
    public void tick() {
        super.tick();

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
        
        particleScale = (1 - (float)age / maxAge) * 1;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getScale(float p_217561_1_) {
        return this.particleScale * MathHelper.clamp(((float)this.age + p_217561_1_) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }

}
