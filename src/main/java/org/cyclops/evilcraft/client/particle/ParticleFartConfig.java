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
 * Config for {@link ParticleFart}.
 * @author rubensworks
 */
public class ParticleFartConfig extends ParticleConfigCommon<ParticleFartData, IModBase> {

    public ParticleFartConfig() {
        super(EvilCraft._instance, "fart", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleFartData> codec() {
                return ParticleFartData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleFartData> streamCodec() {
                return ParticleFartData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleFartData, IModBase> getClientComponent() {
        return new ParticleFartConfigClientComponent();
    }
}
