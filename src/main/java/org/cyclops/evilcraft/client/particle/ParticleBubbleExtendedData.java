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

/**
 * @author rubensworks
 */
public class ParticleBubbleExtendedData implements IParticleData {

    public static final IParticleData.IDeserializer<ParticleBubbleExtendedData> DESERIALIZER = new IParticleData.IDeserializer<ParticleBubbleExtendedData>() {
        public ParticleBubbleExtendedData deserialize(ParticleType<ParticleBubbleExtendedData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float gravity = (float) reader.readDouble();
            return new ParticleBubbleExtendedData(gravity);
        }

        public ParticleBubbleExtendedData read(ParticleType<ParticleBubbleExtendedData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleBubbleExtendedData(buffer.readFloat());
        }
    };
    public static final Codec<ParticleBubbleExtendedData> CODEC = RecordCodecBuilder.create((builder) -> builder
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
        return RegistryEntries.PARTICLE_BUBBLE_EXTENDED;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(gravity);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.gravity);
    }
}
