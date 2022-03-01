package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

/**
 * Orbiting dark smoke effect.
 * @author rubensworks
 * @see SmokeParticle
 */
public class ParticleDarkSmoke extends TextureSheetParticle {

    public ParticleDarkSmoke(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean entityDead) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;

        quadSize = 1;
        alpha = random.nextFloat() * 0.3F;

        rCol = random.nextFloat() * 0.05F + 0.1F;
        gCol = random.nextFloat() * 0.05F;
        bCol = random.nextFloat() * 0.05F + 0.1F;

        gravity = -0.001F;

        this.lifetime = (int)(10.0F / (this.random.nextFloat() * 0.9F + 0.1F));

        if (entityDead) {
            setDeathParticles();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float p_217561_1_) {
        return this.quadSize * Mth.clamp(((float)this.age + p_217561_1_) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
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

        quadSize = (1 - (float)age / lifetime) * 2;
    }

    /**
     * If the particles for this should be shown as death particles.
     */
    public void setDeathParticles() {
        xd *= 2;
        yd *= 2;
        zd *= 2;
        gravity = 0.5F;

        rCol = random.nextFloat() * 0.33125F;
        gCol = random.nextFloat() * 0.022187F;
        bCol = random.nextFloat() * 0.3945F;
    }

}
