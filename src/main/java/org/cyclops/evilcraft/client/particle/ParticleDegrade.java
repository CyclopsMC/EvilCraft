package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * An effect for magical particles.
 * @author rubensworks
 *
 */
public class ParticleDegrade extends SpriteTexturedParticle {

    public ParticleDegrade(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.maxAge = 40;
        this.particleAlpha = 0.4F;
        particleRed = 0.3F + rand.nextFloat() * 0.2F;
        particleGreen = 0.2F + rand.nextFloat() * 0.1F;
        particleBlue = 0.25F + rand.nextFloat() * 0.45F;
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
        
        this.motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
        this.motionY = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        this.motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
        
        particleScale = (1 - (float)age / maxAge) * 0.9F;
    }

}
