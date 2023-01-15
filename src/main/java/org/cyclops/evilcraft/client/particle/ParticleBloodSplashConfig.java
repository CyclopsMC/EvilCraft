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
 * Config for {@link ParticleBloodSplash}.
 * @author rubensworks
 */
public class ParticleBloodSplashConfig extends ParticleConfig<SimpleParticleType> {

    public ParticleBloodSplashConfig() {
        super(EvilCraft._instance, "blood_splash", eConfig -> new SimpleParticleType(false));
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
            ParticleBloodSplash particle = new ParticleBloodSplash(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
