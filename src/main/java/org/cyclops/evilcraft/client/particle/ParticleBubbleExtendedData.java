package org.cyclops.evilcraft.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Locale;

/**
 * @author rubensworks
 */
public class ParticleBubbleExtendedData implements ParticleOptions {

    public static final ParticleOptions.Deserializer<ParticleBubbleExtendedData> DESERIALIZER = new ParticleOptions.Deserializer<ParticleBubbleExtendedData>() {
        public ParticleBubbleExtendedData fromCommand(ParticleType<ParticleBubbleExtendedData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float gravity = (float) reader.readDouble();
            return new ParticleBubbleExtendedData(gravity);
        }

        public ParticleBubbleExtendedData fromNetwork(ParticleType<ParticleBubbleExtendedData> particleTypeIn, FriendlyByteBuf buffer) {
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
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(gravity);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.gravity);
    }
}
