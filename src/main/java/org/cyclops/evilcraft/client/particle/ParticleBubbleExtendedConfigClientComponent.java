package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
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
    public ParticleEngine.SpriteParticleRegistration<ParticleBubbleExtendedData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleBubbleExtendedData>) (particleData, world, x, y, z, motionX, motionY, motionZ) -> {
            ParticleBubbleExtended particle = new ParticleBubbleExtended(world, x, y, z, motionX, motionY, motionZ, particleData.getGravity());
            particle.pickSprite(sprite);
            return particle;
        };
    }
}
