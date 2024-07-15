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
public class ParticleExplosionExtendedData implements ParticleOptions {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleExplosionExtendedData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleExplosionExtendedData::getR,
            ByteBufCodecs.FLOAT, ParticleExplosionExtendedData::getG,
            ByteBufCodecs.FLOAT, ParticleExplosionExtendedData::getB,
            ByteBufCodecs.FLOAT, ParticleExplosionExtendedData::getAlpha,
            ParticleExplosionExtendedData::new
    );
    public static final MapCodec<ParticleExplosionExtendedData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("r").forGetter(ParticleExplosionExtendedData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleExplosionExtendedData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleExplosionExtendedData::getB),
                    Codec.FLOAT.fieldOf("alpha").forGetter(ParticleExplosionExtendedData::getAlpha)
            )
            .apply(builder, ParticleExplosionExtendedData::new));

    private final float r;
    private final float g;
    private final float b;
    private final float alpha;

    public ParticleExplosionExtendedData(float r, float g, float b, float alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
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

    public float getAlpha() {
        return alpha;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_EXPLOSION_EXTENDED.get();
    }
}
