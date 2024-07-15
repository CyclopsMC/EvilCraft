package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleDistort}.
 * @author rubensworks
 */
public class ParticleDistortConfig extends ParticleConfig<ParticleDistortData> {

    public ParticleDistortConfig() {
        super(EvilCraft._instance, "distort", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleDistortData> codec() {
                return ParticleDistortData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleDistortData> streamCodec() {
                return ParticleDistortData.STREAM_CODEC;
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
