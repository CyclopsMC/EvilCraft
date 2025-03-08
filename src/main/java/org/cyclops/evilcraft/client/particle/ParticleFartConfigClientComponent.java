package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleFartConfigClientComponent extends ParticleConfigComponentClient<ParticleFartData, IModBase> {

    @Nullable
    @Override
    public ParticleProvider<ParticleFartData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleFartData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleFartData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleFart particle = new ParticleFart(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.getRainbow());
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
