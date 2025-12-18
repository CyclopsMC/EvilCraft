package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
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
    public ParticleResources.SpriteParticleRegistration<ParticleFartData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleFartData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, random) -> new ParticleFart(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.getRainbow(), sprite.get(random));
    }

}
