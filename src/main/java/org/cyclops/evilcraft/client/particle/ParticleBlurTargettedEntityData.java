package org.cyclops.evilcraft.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Locale;

/**
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityData extends ParticleBlurData {

    public static final Deserializer<ParticleBlurTargettedEntityData> DESERIALIZER = new Deserializer<ParticleBlurTargettedEntityData>() {
        public ParticleBlurTargettedEntityData fromCommand(ParticleType<ParticleBlurTargettedEntityData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float red = (float)reader.readDouble();
            reader.expect(' ');
            float green = (float)reader.readDouble();
            reader.expect(' ');
            float blue = (float)reader.readDouble();
            reader.expect(' ');
            float scale = (float)reader.readDouble();
            reader.expect(' ');
            float ageMultiplier = (float)reader.readDouble();
            reader.expect(' ');
            int entityId = reader.readInt();
            return new ParticleBlurTargettedEntityData(red, green, blue, scale, ageMultiplier, entityId);
        }

        public ParticleBlurTargettedEntityData fromNetwork(ParticleType<ParticleBlurTargettedEntityData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleBlurTargettedEntityData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
        }
    };
    public static final Codec<ParticleBlurTargettedEntityData> CODEC = RecordCodecBuilder.create((builder) -> builder
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
        return RegistryEntries.PARTICLE_BLUR_TARGETTED_ENTITY;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        super.writeToNetwork(buffer);
        buffer.writeInt(entityId);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s",
                super.writeToString(),
                this.entityId);
    }
}
