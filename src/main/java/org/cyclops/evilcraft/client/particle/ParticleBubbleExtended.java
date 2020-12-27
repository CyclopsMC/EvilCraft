package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * {@link BubbleParticle} that has a modifiable gravity factor.
 * The higher this factor, the more quickly it will drop.
 * @author Ruben Taelman
 */
@OnlyIn(Dist.CLIENT)
public class ParticleBubbleExtended extends SpriteTexturedParticle {

    private final float gravity;

    public ParticleBubbleExtended(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, float gravity) {
        super(world, x, y, z);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
        this.motionX = motionX * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.motionY = motionY * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.motionZ = motionZ * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.maxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.gravity = gravity;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= gravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;

        if (this.maxAge-- <= 0) {
            this.setExpired();
        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
