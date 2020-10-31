package org.cyclops.evilcraft.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Locale;

/**
 * @author rubensworks
 */
public class ParticleBloodBrickData implements IParticleData {

    public static final IParticleData.IDeserializer<ParticleBloodBrickData> DESERIALIZER = new IParticleData.IDeserializer<ParticleBloodBrickData>() {
        public ParticleBloodBrickData deserialize(ParticleType<ParticleBloodBrickData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            Direction side = Direction.values()[reader.readInt()];
            return new ParticleBloodBrickData(side);
        }

        public ParticleBloodBrickData read(ParticleType<ParticleBloodBrickData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleBloodBrickData(Direction.values()[buffer.readInt()]);
        }
    };

    private final Direction side;

    public ParticleBloodBrickData(Direction side) {
        this.side = side;
    }

    public Direction getSide() {
        return side;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_BLOOD_BRICK;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(side.ordinal());
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.side.ordinal());
    }
}
