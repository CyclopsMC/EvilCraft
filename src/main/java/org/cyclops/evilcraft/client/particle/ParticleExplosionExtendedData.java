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
public class ParticleExplosionExtendedData implements ParticleOptions {

    public static final Deserializer<ParticleExplosionExtendedData> DESERIALIZER = new Deserializer<ParticleExplosionExtendedData>() {
        public ParticleExplosionExtendedData fromCommand(ParticleType<ParticleExplosionExtendedData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = (float) reader.readDouble();
            reader.expect(' ');
            float g = (float) reader.readDouble();
            reader.expect(' ');
            float b = (float) reader.readDouble();
            reader.expect(' ');
            float alpha = (float) reader.readDouble();
            return new ParticleExplosionExtendedData(r, g, b, alpha);
        }

        public ParticleExplosionExtendedData fromNetwork(ParticleType<ParticleExplosionExtendedData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleExplosionExtendedData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };
    public static final Codec<ParticleExplosionExtendedData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("r").forGetter(ParticleExplosionExtendedData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleExplosionExtendedData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleExplosionExtendedData::getB),
                    Codec.FLOAT.fieldOf("alpha").forGetter(ParticleExplosionExtendedData::getAlpha)
            )
            .apply(builder, ParticleExplosionExtendedData::new));

    private final float r;
    private final float g;
    private final float b;
    private final float alpha;

    public ParticleExplosionExtendedData(float r, float g, float b, float alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
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

    public float getAlpha() {
        return alpha;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_EXPLOSION_EXTENDED;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(r);
        buffer.writeFloat(g);
        buffer.writeFloat(b);
        buffer.writeFloat(alpha);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.r, this.g, this.b, this.alpha);
    }
}
