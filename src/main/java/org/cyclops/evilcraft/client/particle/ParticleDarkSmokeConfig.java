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
 * Config for {@link ParticleDarkSmoke}.
 * @author rubensworks
 */
public class ParticleDarkSmokeConfig extends ParticleConfig<ParticleDarkSmokeData> {

    public ParticleDarkSmokeConfig() {
        super(EvilCraft._instance, "dark_smoke", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleDarkSmokeData> codec() {
                return ParticleDarkSmokeData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleDarkSmokeData> streamCodec() {
                return ParticleDarkSmokeData.STREAM_CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleDarkSmokeData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleDarkSmokeData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleDarkSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleDarkSmoke particle = new ParticleDarkSmoke(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.isEntityDead());
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
