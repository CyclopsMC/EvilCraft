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
public class ParticleDarkSmokeData implements ParticleOptions {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleDarkSmokeData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ParticleDarkSmokeData::isEntityDead,
            ParticleDarkSmokeData::new
    );
    public static final MapCodec<ParticleDarkSmokeData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.BOOL.fieldOf("dead").forGetter(ParticleDarkSmokeData::isEntityDead)
            )
            .apply(builder, ParticleDarkSmokeData::new));

    private final boolean entityDead;

    public ParticleDarkSmokeData(boolean entityDead) {
        this.entityDead = entityDead;
    }

    public boolean isEntityDead() {
        return entityDead;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_DARK_SMOKE.get();
    }
}
