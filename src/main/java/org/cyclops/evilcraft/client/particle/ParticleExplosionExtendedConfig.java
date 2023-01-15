package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleExplosionExtended}.
 * @author rubensworks
 */
public class ParticleExplosionExtendedConfig extends ParticleConfig<ParticleExplosionExtendedData> {

    public ParticleExplosionExtendedConfig() {
        super(EvilCraft._instance, "explosion_extended", eConfig -> new ParticleType<ParticleExplosionExtendedData>(false, ParticleExplosionExtendedData.DESERIALIZER) {

            @Override
            public Codec<ParticleExplosionExtendedData> codec() {
                return ParticleExplosionExtendedData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleExplosionExtendedData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleExplosionExtendedData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleExplosionExtendedData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleExplosionExtended particle = new ParticleExplosionExtended(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
                    particleData.getR(), particleData.getG(), particleData.getB(), particleData.getAlpha(), sprite);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
