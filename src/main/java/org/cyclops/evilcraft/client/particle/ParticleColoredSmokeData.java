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
public class ParticleColoredSmokeData implements ParticleOptions {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleColoredSmokeData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleColoredSmokeData::getR,
            ByteBufCodecs.FLOAT, ParticleColoredSmokeData::getG,
            ByteBufCodecs.FLOAT, ParticleColoredSmokeData::getB,
            ParticleColoredSmokeData::new
    );
    public static final MapCodec<ParticleColoredSmokeData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("r").forGetter(ParticleColoredSmokeData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleColoredSmokeData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleColoredSmokeData::getB)
            )
            .apply(builder, ParticleColoredSmokeData::new));

    private final float r;
    private final float g;
    private final float b;

    public ParticleColoredSmokeData(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_COLORED_SMOKE.get();
    }
}
