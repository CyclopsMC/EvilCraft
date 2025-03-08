package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ParticleDistort}.
 * @author rubensworks
 */
public class ParticleDistortConfig extends ParticleConfigCommon<ParticleDistortData, IModBase> {

    public ParticleDistortConfig() {
        super(EvilCraft._instance, "distort", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleDistortData> codec() {
                return ParticleDistortData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleDistortData> streamCodec() {
                return ParticleDistortData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleDistortData, IModBase> getClientComponent() {
        return new ParticleDistortConfigComponentClient();
    }
}
