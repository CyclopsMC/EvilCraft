package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleMagicFinish}.
 * @author rubensworks
 */
public class ParticleMagicFinishConfig extends ParticleConfig<SimpleParticleType> {

    public ParticleMagicFinishConfig() {
        super(EvilCraft._instance, "magic_finish", eConfig -> new SimpleParticleType(false));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<SimpleParticleType> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<SimpleParticleType> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<SimpleParticleType>) (typeIn, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleMagicFinish particle = new ParticleMagicFinish(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
