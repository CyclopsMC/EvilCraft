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
public class ParticleFartData implements ParticleOptions {

    public static final Deserializer<ParticleFartData> DESERIALIZER = new Deserializer<ParticleFartData>() {
        public ParticleFartData fromCommand(ParticleType<ParticleFartData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            boolean rainbow = reader.readBoolean();
            return new ParticleFartData(rainbow);
        }

        public ParticleFartData fromNetwork(ParticleType<ParticleFartData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleFartData(buffer.readBoolean());
        }
    };
    public static final Codec<ParticleFartData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    Codec.BOOL.fieldOf("rainbow").forGetter(ParticleFartData::getRainbow)
            )
            .apply(builder, ParticleFartData::new));

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
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(rainbow);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.rainbow);
    }
}
