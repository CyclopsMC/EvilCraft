package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * @author rubensworks
 */
public class ParticleDistortData implements ParticleOptions {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleDistortData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleDistortData::getScale,
            ParticleDistortData::new
    );
    public static final MapCodec<ParticleDistortData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleDistortData::getScale)
            )
            .apply(builder, ParticleDistortData::new));

    private final float scale;

    public ParticleDistortData(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_DISTORT.get();
    }
}
