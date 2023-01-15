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
public class ParticleDistortData implements ParticleOptions {

    public static final Deserializer<ParticleDistortData> DESERIALIZER = new Deserializer<ParticleDistortData>() {
        public ParticleDistortData fromCommand(ParticleType<ParticleDistortData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float scale = (float) reader.readDouble();
            return new ParticleDistortData(scale);
        }

        public ParticleDistortData fromNetwork(ParticleType<ParticleDistortData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleDistortData(buffer.readFloat());
        }
    };
    public static final Codec<ParticleDistortData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleDistortData::getScale)
            )
            .apply(builder, ParticleDistortData::new));

    private final float scale;

    public ParticleDistortData(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_DISTORT;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(scale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.scale);
    }
}
