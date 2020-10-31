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
public class ParticleDarkSmokeData implements IParticleData {

    public static final IDeserializer<ParticleDarkSmokeData> DESERIALIZER = new IDeserializer<ParticleDarkSmokeData>() {
        public boolean entityDead;

        public ParticleDarkSmokeData deserialize(ParticleType<ParticleDarkSmokeData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            entityDead = reader.readBoolean();
            return new ParticleDarkSmokeData(entityDead);
        }

        public ParticleDarkSmokeData read(ParticleType<ParticleDarkSmokeData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleDarkSmokeData(buffer.readBoolean());
        }
    };

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
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(entityDead);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.entityDead);
    }
}
