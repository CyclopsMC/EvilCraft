package org.cyclops.evilcraft.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Locale;

/**
 * @author rubensworks
 */
public class ParticleExplosionExtendedData implements IParticleData {

    public static final IDeserializer<ParticleExplosionExtendedData> DESERIALIZER = new IDeserializer<ParticleExplosionExtendedData>() {
        public ParticleExplosionExtendedData deserialize(ParticleType<ParticleExplosionExtendedData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = (float) reader.readDouble();
            reader.expect(' ');
            float g = (float) reader.readDouble();
            reader.expect(' ');
            float b = (float) reader.readDouble();
            reader.expect(' ');
            float scale = (float) reader.readDouble();
            return new ParticleExplosionExtendedData(r, g, b, scale);
        }

        public ParticleExplosionExtendedData read(ParticleType<ParticleExplosionExtendedData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleExplosionExtendedData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

    private final float r;
    private final float g;
    private final float b;
    private final float scale;

    public ParticleExplosionExtendedData(float r, float g, float b, float scale) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.scale = scale;
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

    public float getScale() {
        return scale;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_EXPLOSION_EXTENDED;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(r);
        buffer.writeFloat(g);
        buffer.writeFloat(b);
        buffer.writeFloat(scale);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.r, this.g, this.b, this.scale);
    }
}
