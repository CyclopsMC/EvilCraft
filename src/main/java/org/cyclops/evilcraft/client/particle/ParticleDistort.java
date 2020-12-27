package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.cyclops.evilcraft.item.ItemMaceOfDistortion;

/**
 * An effect for the distortion radial effect.
 * @author rubensworks
 *
 */
public class ParticleDistort extends SpriteTexturedParticle {

    public ParticleDistort(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale, IAnimatedSprite sprite) {
        super(world, x, y, z, motionX, motionY, motionZ);
        
        particleScale = scale;
        particleAlpha = 0.3F;
        maxAge = ItemMaceOfDistortion.AOE_TICK_UPDATE;
        
        particleRed = 1.0F * rand.nextFloat();
        particleGreen = 0.01F * rand.nextFloat();
        particleBlue = 0.5F * rand.nextFloat();
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

        particleScale = (1 - (float)age / maxAge) * 3;
    }

}
