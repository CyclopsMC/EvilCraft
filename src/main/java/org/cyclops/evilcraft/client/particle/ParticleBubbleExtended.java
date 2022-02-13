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
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.xd = motionX * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.yd = motionY * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.zd = motionZ * (double)0.2F + (Math.random() * 2.0D - 1.0D) * (double)0.02F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.gravity = gravity;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.yd -= gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.85D;
        this.yd *= 0.85D;
        this.zd *= 0.85D;

        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
