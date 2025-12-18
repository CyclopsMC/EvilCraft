package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleExplosionExtendedConfigClientComponent extends ParticleConfigComponentClient<ParticleExplosionExtendedData, IModBase> {

    @Nullable
    @Override
    public ParticleProvider<ParticleExplosionExtendedData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleResources.SpriteParticleRegistration<ParticleExplosionExtendedData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleExplosionExtendedData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, random) -> new ParticleExplosionExtended(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
                particleData.getR(), particleData.getG(), particleData.getB(), particleData.getAlpha(), sprite);
    }

}
