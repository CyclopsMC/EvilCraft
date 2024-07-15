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
 * Config for {@link ParticleBubbleExtended}.
 * @author rubensworks
 */
public class ParticleBubbleExtendedConfig extends ParticleConfig<ParticleBubbleExtendedData> {

    public ParticleBubbleExtendedConfig() {
        super(EvilCraft._instance, "bubble_extended", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleBubbleExtendedData> codec() {
                return ParticleBubbleExtendedData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBubbleExtendedData> streamCodec() {
                return ParticleBubbleExtendedData.STREAM_CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleBubbleExtendedData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleBubbleExtendedData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleBubbleExtendedData>) (particleData, world, x, y, z, motionX, motionY, motionZ) -> {
            ParticleBubbleExtended particle = new ParticleBubbleExtended(world, x, y, z, motionX, motionY, motionZ, particleData.getGravity());
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
