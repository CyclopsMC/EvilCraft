package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleBubbleExtendedConfigClientComponent extends ParticleConfigComponentClient<ParticleBubbleExtendedData, IModBase> {
    @javax.annotation.Nullable
    @Override
    public ParticleProvider<ParticleBubbleExtendedData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleResources.SpriteParticleRegistration<ParticleBubbleExtendedData> getParticleMetaFactory() {
        return sprites -> (particleData, world, x, y, z, motionX, motionY, motionZ, random) -> new ParticleBubbleExtended(world, x, y, z, motionX, motionY, motionZ, particleData.getGravity(), sprites);
    }
}
