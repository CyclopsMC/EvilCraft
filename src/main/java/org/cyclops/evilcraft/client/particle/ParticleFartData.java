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
public class ParticleFartData implements ParticleOptions {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleFartData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ParticleFartData::getRainbow,
            ParticleFartData::new
    );
    public static final MapCodec<ParticleFartData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.BOOL.fieldOf("rainbow").forGetter(ParticleFartData::getRainbow)
            )
            .apply(builder, ParticleFartData::new));

    private final boolean rainbow;

    public ParticleFartData(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean getRainbow() {
        return rainbow;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_FART.get();
    }
}
