package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityData extends ParticleBlurData {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleBlurTargettedEntityData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleBlurTargettedEntityData::getRed,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedEntityData::getGreen,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedEntityData::getBlue,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedEntityData::getScale,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedEntityData::getAgeMultiplier,
            ByteBufCodecs.INT, ParticleBlurTargettedEntityData::getEntityId,
            ParticleBlurTargettedEntityData::new
    );
    public static final MapCodec<ParticleBlurTargettedEntityData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("r").forGetter(ParticleBlurTargettedEntityData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleBlurTargettedEntityData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleBlurTargettedEntityData::getBlue),
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleBlurTargettedEntityData::getScale),
                    Codec.FLOAT.fieldOf("age").forGetter(ParticleBlurTargettedEntityData::getAgeMultiplier),
                    Codec.INT.fieldOf("entity").forGetter(ParticleBlurTargettedEntityData::getEntityId)
            )
            .apply(builder, ParticleBlurTargettedEntityData::new));

    private final int entityId;

    public ParticleBlurTargettedEntityData(float red, float green, float blue, float scale, float ageMultiplier, int entityId) {
        super(red, green, blue, scale, ageMultiplier);
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_BLUR_TARGETTED_ENTITY.get();
    }
}
