package org.cyclops.evilcraft.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleType;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Locale;

/**
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityData extends ParticleBlurData {

    public static final IDeserializer<ParticleBlurTargettedEntityData> DESERIALIZER = new IDeserializer<ParticleBlurTargettedEntityData>() {
        public ParticleBlurTargettedEntityData deserialize(ParticleType<ParticleBlurTargettedEntityData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float red = (float)reader.readDouble();
            reader.expect(' ');
            float green = (float)reader.readDouble();
            reader.expect(' ');
            float blue = (float)reader.readDouble();
            reader.expect(' ');
            float scale = (float)reader.readDouble();
            reader.expect(' ');
            float ageMultiplier = (float)reader.readDouble();
            reader.expect(' ');
            int entityId = reader.readInt();
            return new ParticleBlurTargettedEntityData(red, green, blue, scale, ageMultiplier, entityId);
        }

        public ParticleBlurTargettedEntityData read(ParticleType<ParticleBlurTargettedEntityData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleBlurTargettedEntityData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
        }
    };

    private final int entityId;

    public ParticleBlurTargettedEntityData(float red, float green, float blue, float scale, float ageMultiplier, int entityId) {
        super(red, green, blue, scale, ageMultiplier);
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_EXPLOSION_EXTENDED;
    }

    @Override
    public void write(PacketBuffer buffer) {
        super.write(buffer);
        buffer.writeInt(entityId);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %s",
                super.getParameters(),
                this.entityId);
    }
}
