package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleColoredSmokeConfigClientComponent extends ParticleConfigComponentClient<ParticleColoredSmokeData, IModBase> {

    @Nullable
    @Override
    public ParticleProvider<ParticleColoredSmokeData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleColoredSmokeData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleColoredSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleColoredSmoke particle = new ParticleColoredSmoke(worldIn, x, y, z,
                    particleData.getR(), particleData.getG(), particleData.getB(),
                    xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
