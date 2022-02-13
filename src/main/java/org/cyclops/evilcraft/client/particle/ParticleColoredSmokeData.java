package org.cyclops.evilcraft.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Locale;

import net.minecraft.particles.IParticleData.IDeserializer;

/**
 * @author rubensworks
 */
public class ParticleColoredSmokeData implements IParticleData {

    public static final IDeserializer<ParticleColoredSmokeData> DESERIALIZER = new IDeserializer<ParticleColoredSmokeData>() {
        public ParticleColoredSmokeData fromCommand(ParticleType<ParticleColoredSmokeData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = (float) reader.readDouble();
            reader.expect(' ');
            float g = (float) reader.readDouble();
            reader.expect(' ');
            float b = (float) reader.readDouble();
            return new ParticleColoredSmokeData(r, g, b);
        }

        public ParticleColoredSmokeData fromNetwork(ParticleType<ParticleColoredSmokeData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleColoredSmokeData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };
    public static final Codec<ParticleColoredSmokeData> CODEC = RecordCodecBuilder.create((builder) -> builder
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
        return RegistryEntries.PARTICLE_COLORED_SMOKE;
    }

    @Override
    public void writeToNetwork(PacketBuffer buffer) {
        buffer.writeFloat(r);
        buffer.writeFloat(g);
        buffer.writeFloat(b);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.r, this.g, this.b);
    }
}
