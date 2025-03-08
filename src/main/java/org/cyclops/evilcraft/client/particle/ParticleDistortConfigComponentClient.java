package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleDistortConfigComponentClient extends ParticleConfigComponentClient<ParticleDistortData, IModBase> {

    @Nullable
    @Override
    public ParticleProvider<ParticleDistortData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleDistortData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleDistortData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleDistort particle = new ParticleDistort(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.getScale(), sprite);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
