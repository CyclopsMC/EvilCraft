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
public class ParticleFartData implements IParticleData {

    public static final IDeserializer<ParticleFartData> DESERIALIZER = new IDeserializer<ParticleFartData>() {
        public ParticleFartData deserialize(ParticleType<ParticleFartData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            boolean rainbow = reader.readBoolean();
            return new ParticleFartData(rainbow);
        }

        public ParticleFartData read(ParticleType<ParticleFartData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleFartData(buffer.readBoolean());
        }
    };

    private final boolean rainbow;

    public ParticleFartData(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean getRainbow() {
        return rainbow;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_FART;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(rainbow);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %s",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.rainbow);
    }
}
