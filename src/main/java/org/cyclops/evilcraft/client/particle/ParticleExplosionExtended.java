package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

/**
 * An extended {@link ExplodeParticle}
 * @author rubensworks
 *
 */
public class ParticleExplosionExtended extends ExplodeParticle {

    public ParticleExplosionExtended(ClientLevel world, double x, double y, double z,
                                     double motionX, double motionY, double motionZ,
                                     float red, float green, float blue, float alpha,
                                     SpriteSet sprite) {
        super(world, x, y, z, motionX, motionY, motionZ, sprite);
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = alpha;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

}
