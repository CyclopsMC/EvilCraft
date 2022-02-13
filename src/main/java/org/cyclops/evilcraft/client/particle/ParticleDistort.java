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
        
        quadSize = scale;
        alpha = 0.3F;
        lifetime = ItemMaceOfDistortion.AOE_TICK_UPDATE;
        
        rCol = 1.0F * random.nextFloat();
        gCol = 0.01F * random.nextFloat();
        bCol = 0.5F * random.nextFloat();
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

        quadSize = (1 - (float)age / lifetime) * 3;
    }

}
