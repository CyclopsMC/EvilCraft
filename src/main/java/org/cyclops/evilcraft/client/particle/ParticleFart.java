package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

/**
 * A fart special effect.
 * @author immortaleeb
 *
 */
public class ParticleFart extends TextureSheetParticle {

    public ParticleFart(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean rainbow) {
        super(world, x, y, z, motionX, motionY, motionZ);

        quadSize = 0.25F;
        alpha = 0.7F;

        if (!rainbow) {
            rCol = 0.50F + random.nextFloat() * 0.2F;
            gCol = 0.3F + random.nextFloat() * 0.1F;
            bCol = random.nextFloat() * 0.2F;
        } else {
            rCol = random.nextFloat();
            gCol = random.nextFloat();
            bCol = random.nextFloat();
        }
    }

    @Override
    public void tick() {
        super.tick();

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

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float p_217561_1_) {
        return this.quadSize * Mth.clamp(((float)this.age + p_217561_1_) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

}
