package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleDarkSmokeConfigClientComponent extends ParticleConfigComponentClient<ParticleDarkSmokeData, IModBase> {
    @javax.annotation.Nullable
    @Override
    public ParticleProvider<ParticleDarkSmokeData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleDarkSmokeData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleDarkSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleDarkSmoke particle = new ParticleDarkSmoke(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.isEntityDead());
            particle.pickSprite(sprite);
            return particle;
        };
    }
}
