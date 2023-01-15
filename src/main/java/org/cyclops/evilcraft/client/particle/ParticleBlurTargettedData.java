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
public class ParticleBlurTargettedData extends ParticleBlurData {

    public static final Deserializer<ParticleBlurTargettedData> DESERIALIZER = new Deserializer<ParticleBlurTargettedData>() {
        public ParticleBlurTargettedData fromCommand(ParticleType<ParticleBlurTargettedData> particleType, StringReader reader) throws CommandSyntaxException {
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
            float targetX = (float) reader.readDouble();
            reader.expect(' ');
            float targetY = (float) reader.readDouble();
            reader.expect(' ');
            float targetZ = (float) reader.readDouble();
            return new ParticleBlurTargettedData(red, green, blue, scale, ageMultiplier, targetX, targetY, targetZ);
        }

        public ParticleBlurTargettedData fromNetwork(ParticleType<ParticleBlurTargettedData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleBlurTargettedData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };
    public static final Codec<ParticleBlurTargettedData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("r").forGetter(ParticleBlurTargettedData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleBlurTargettedData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleBlurTargettedData::getBlue),
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleBlurTargettedData::getScale),
                    Codec.FLOAT.fieldOf("age").forGetter(ParticleBlurTargettedData::getAgeMultiplier),
                    Codec.FLOAT.fieldOf("x").forGetter(ParticleBlurTargettedData::getTargetX),
                    Codec.FLOAT.fieldOf("y").forGetter(ParticleBlurTargettedData::getTargetY),
                    Codec.FLOAT.fieldOf("z").forGetter(ParticleBlurTargettedData::getTargetZ)
            )
            .apply(builder, ParticleBlurTargettedData::new));

    private final float targetX;
    private final float targetY;
    private final float targetZ;

    public ParticleBlurTargettedData(float red, float green, float blue, float scale, float ageMultiplier, float targetX, float targetY, float targetZ) {
        super(red, green, blue, scale, ageMultiplier);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_BLUR_TARGETTED;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        super.writeToNetwork(buffer);
        buffer.writeFloat(targetX);
        buffer.writeFloat(targetY);
        buffer.writeFloat(targetZ);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f",
                super.writeToString(),
                this.targetX, this.targetY, this.targetZ);
    }
}
