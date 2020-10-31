package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Smoke effect.
 * @author rubensworks
 * @see SmokeParticle
 */
public class ParticleColoredSmoke extends SpriteTexturedParticle {

    private final IAnimatedSprite sprite;

    public ParticleColoredSmoke(World world, double x, double y, double z, float r, float g, float b,
                                double motionX, double motionY, double motionZ, IAnimatedSprite sprite) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        particleScale = 1;
        particleAlpha = rand.nextFloat() * 0.3F;
        particleGravity = -0.001F;
        this.maxAge = (int)(50.0F / (this.rand.nextFloat() * 0.9F + 0.1F));

        this.sprite = sprite;
        this.selectSpriteWithAge(sprite);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
            this.selectSpriteWithAge(this.sprite);
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
    }

}
