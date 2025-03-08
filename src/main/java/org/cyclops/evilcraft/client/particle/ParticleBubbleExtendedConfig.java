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
 * Config for {@link ParticleBubbleExtended}.
 * @author rubensworks
 */
public class ParticleBubbleExtendedConfig extends ParticleConfigCommon<ParticleBubbleExtendedData, IModBase> {

    public ParticleBubbleExtendedConfig() {
        super(EvilCraft._instance, "bubble_extended", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleBubbleExtendedData> codec() {
                return ParticleBubbleExtendedData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBubbleExtendedData> streamCodec() {
                return ParticleBubbleExtendedData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleBubbleExtendedData, IModBase> getClientComponent() {
        return new ParticleBubbleExtendedConfigClientComponent();
    }
}
