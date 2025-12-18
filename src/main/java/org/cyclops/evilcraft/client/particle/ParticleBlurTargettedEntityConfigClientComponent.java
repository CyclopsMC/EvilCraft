package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityConfigClientComponent extends ParticleConfigComponentClient<ParticleBlurTargettedEntityData, IModBase> {
    @javax.annotation.Nullable
    @Override
    public ParticleProvider<ParticleBlurTargettedEntityData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleResources.SpriteParticleRegistration<ParticleBlurTargettedEntityData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleBlurTargettedEntityData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, random) -> new ParticleBlurTargettedEntity(particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, sprite.get(random));
    }
}
