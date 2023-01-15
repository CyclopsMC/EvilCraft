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
 * Config for {@link ParticleDistort}.
 * @author rubensworks
 */
public class ParticleDistortConfig extends ParticleConfig<ParticleDistortData> {

    public ParticleDistortConfig() {
        super(EvilCraft._instance, "distort", eConfig -> new ParticleType<ParticleDistortData>(false, ParticleDistortData.DESERIALIZER) {

            @Override
            public Codec<ParticleDistortData> codec() {
                return ParticleDistortData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleDistortData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
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
