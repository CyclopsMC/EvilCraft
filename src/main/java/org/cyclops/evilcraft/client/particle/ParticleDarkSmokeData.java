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
public class ParticleDarkSmokeData implements ParticleOptions {

    public static final Deserializer<ParticleDarkSmokeData> DESERIALIZER = new Deserializer<ParticleDarkSmokeData>() {
        public boolean entityDead;

        public ParticleDarkSmokeData fromCommand(ParticleType<ParticleDarkSmokeData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            entityDead = reader.readBoolean();
            return new ParticleDarkSmokeData(entityDead);
        }

        public ParticleDarkSmokeData fromNetwork(ParticleType<ParticleDarkSmokeData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleDarkSmokeData(buffer.readBoolean());
        }
    };
    public static final Codec<ParticleDarkSmokeData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    Codec.BOOL.fieldOf("dead").forGetter(ParticleDarkSmokeData::isEntityDead)
            )
            .apply(builder, ParticleDarkSmokeData::new));

    private final boolean entityDead;

    public ParticleDarkSmokeData(boolean entityDead) {
        this.entityDead = entityDead;
    }

    public boolean isEntityDead() {
        return entityDead;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_DARK_SMOKE;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(entityDead);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.entityDead);
    }
}
