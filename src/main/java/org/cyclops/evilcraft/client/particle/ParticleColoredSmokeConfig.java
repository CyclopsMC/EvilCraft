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
 * Config for {@link ParticleColoredSmoke}.
 * @author rubensworks
 */
public class ParticleColoredSmokeConfig extends ParticleConfig<ParticleColoredSmokeData> {

    public ParticleColoredSmokeConfig() {
        super(EvilCraft._instance, "colored_smoke", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleColoredSmokeData> codec() {
                return ParticleColoredSmokeData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleColoredSmokeData> streamCodec() {
                return ParticleColoredSmokeData.STREAM_CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleColoredSmokeData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleColoredSmokeData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleColoredSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleColoredSmoke particle = new ParticleColoredSmoke(worldIn, x, y, z,
                    particleData.getR(), particleData.getG(), particleData.getB(),
                    xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
