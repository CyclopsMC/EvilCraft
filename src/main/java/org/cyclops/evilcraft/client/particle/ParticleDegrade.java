package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

/**
 * An effect for magical particles.
 * @author rubensworks
 *
 */
public class ParticleDegrade extends TextureSheetParticle {

    public ParticleDegrade(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.lifetime = 40;
        this.alpha = 0.4F;
        rCol = 0.3F + random.nextFloat() * 0.2F;
        gCol = 0.2F + random.nextFloat() * 0.1F;
        bCol = 0.25F + random.nextFloat() * 0.45F;
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

        this.xd = (random.nextFloat() - random.nextFloat()) * 0.2F;
        this.yd = (random.nextFloat() - random.nextFloat()) * 0.1F;
        this.zd = (random.nextFloat() - random.nextFloat()) * 0.2F;

        quadSize = (1 - (float)age / lifetime) * 0.9F;
    }

}
