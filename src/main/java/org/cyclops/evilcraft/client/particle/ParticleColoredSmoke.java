package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Smoke effect.
 * @author rubensworks
 * @see SmokeParticle
 */
public class ParticleColoredSmoke extends SpriteTexturedParticle {

    public ParticleColoredSmoke(ClientWorld world, double x, double y, double z, float r, float g, float b,
                                double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;

        quadSize = 1;
        alpha = random.nextFloat() * 0.3F;
        gravity = -0.001F;
        this.lifetime = (int)(50.0F / (this.random.nextFloat() * 0.9F + 0.1F));
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float p_217561_1_) {
        return this.quadSize * MathHelper.clamp(((float)this.age + p_217561_1_) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd += 0.004D;
            this.move(this.xd, this.yd, this.zd);
            if (this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= (double)0.96F;
            this.yd *= (double)0.96F;
            this.zd *= (double)0.96F;
            if (this.onGround) {
                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

        }
    }

}
