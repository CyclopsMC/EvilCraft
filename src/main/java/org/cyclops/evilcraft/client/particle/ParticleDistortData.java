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
public class ParticleDistortData implements IParticleData {

    public static final IDeserializer<ParticleDistortData> DESERIALIZER = new IDeserializer<ParticleDistortData>() {
        public ParticleDistortData deserialize(ParticleType<ParticleDistortData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float gravity = (float) reader.readDouble();
            return new ParticleDistortData(gravity);
        }

        public ParticleDistortData read(ParticleType<ParticleDistortData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleDistortData(buffer.readFloat());
        }
    };

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
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(scale);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.scale);
    }
}
