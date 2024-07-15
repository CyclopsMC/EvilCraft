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
public class ParticleBubbleExtendedData implements ParticleOptions {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleBubbleExtendedData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleBubbleExtendedData::getGravity,
            ParticleBubbleExtendedData::new
    );
    public static final MapCodec<ParticleBubbleExtendedData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("gravity").forGetter(ParticleBubbleExtendedData::getGravity)
            )
            .apply(builder, ParticleBubbleExtendedData::new));

    private final float gravity;

    public ParticleBubbleExtendedData(float gravity) {
        this.gravity = gravity;
    }

    public float getGravity() {
        return gravity;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_BUBBLE_EXTENDED.get();
    }
}
