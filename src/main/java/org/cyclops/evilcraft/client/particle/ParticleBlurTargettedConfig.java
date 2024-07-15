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
 * Config for {@link ParticleBlurTargetted}.
 * @author rubensworks
 */
public class ParticleBlurTargettedConfig extends ParticleConfig<ParticleBlurTargettedData> {

    public ParticleBlurTargettedConfig() {
        super(EvilCraft._instance, "blur_targetted", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleBlurTargettedData> codec() {
                return ParticleBlurTargettedData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBlurTargettedData> streamCodec() {
                return ParticleBlurTargettedData.STREAM_CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleBlurTargettedData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
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
