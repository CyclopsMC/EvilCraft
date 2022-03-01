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
 * Config for {@link ParticleBubbleExtended}.
 * @author rubensworks
 */
public class ParticleBubbleExtendedConfig extends ParticleConfig<ParticleBubbleExtendedData> {

    public ParticleBubbleExtendedConfig() {
        super(EvilCraft._instance, "bubble_extended", eConfig -> new ParticleType<ParticleBubbleExtendedData>(false, ParticleBubbleExtendedData.DESERIALIZER) {

            @Override
            public Codec<ParticleBubbleExtendedData> codec() {
                return ParticleBubbleExtendedData.CODEC;
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
