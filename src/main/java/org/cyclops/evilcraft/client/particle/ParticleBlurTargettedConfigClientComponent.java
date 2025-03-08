package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleBlurTargettedConfigClientComponent extends ParticleConfigComponentClient<ParticleBlurTargettedData, IModBase> {
    @javax.annotation.Nullable
    @Override
    public ParticleProvider<ParticleBlurTargettedData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleBlurTargettedData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleBlurTargettedData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleBlurTargetted particle = new ParticleBlurTargetted(particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }
}
