package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.core.particles.SimpleParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleBloodBubbleConfigComponentClient extends ParticleConfigComponentClient<SimpleParticleType, IModBase> {
    @javax.annotation.Nullable
    @Override
    public ParticleProvider<SimpleParticleType> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleResources.SpriteParticleRegistration<SimpleParticleType> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<SimpleParticleType>) (typeIn, worldIn, x, y, z, xSpeed, ySpeed, zSpeed, random) -> new ParticleBloodBubble(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, sprite.get(random));
    }
}
