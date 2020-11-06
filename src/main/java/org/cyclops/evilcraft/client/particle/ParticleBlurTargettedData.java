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
public class ParticleBlurTargettedData extends ParticleBlurData {

    public static final IDeserializer<ParticleBlurTargettedData> DESERIALIZER = new IDeserializer<ParticleBlurTargettedData>() {
        public ParticleBlurTargettedData deserialize(ParticleType<ParticleBlurTargettedData> particleType, StringReader reader) throws CommandSyntaxException {
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
            float targetX = (float) reader.readDouble();
            reader.expect(' ');
            float targetY = (float) reader.readDouble();
            reader.expect(' ');
            float targetZ = (float) reader.readDouble();
            return new ParticleBlurTargettedData(red, green, blue, scale, ageMultiplier, targetX, targetY, targetZ);
        }

        public ParticleBlurTargettedData read(ParticleType<ParticleBlurTargettedData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleBlurTargettedData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

    private final float targetX;
    private final float targetY;
    private final float targetZ;

    public ParticleBlurTargettedData(float red, float green, float blue, float scale, float ageMultiplier, float targetX, float targetY, float targetZ) {
        super(red, green, blue, scale, ageMultiplier);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_BLUR_TARGETTED;
    }

    @Override
    public void write(PacketBuffer buffer) {
        super.write(buffer);
        buffer.writeFloat(targetX);
        buffer.writeFloat(targetY);
        buffer.writeFloat(targetZ);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f",
                super.getParameters(),
                this.targetX, this.targetY, this.targetZ);
    }
}
