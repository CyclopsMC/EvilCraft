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
 * Config for {@link ParticleDarkSmoke}.
 * @author rubensworks
 */
public class ParticleDarkSmokeConfig extends ParticleConfigCommon<ParticleDarkSmokeData, IModBase> {

    public ParticleDarkSmokeConfig() {
        super(EvilCraft._instance, "dark_smoke", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleDarkSmokeData> codec() {
                return ParticleDarkSmokeData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleDarkSmokeData> streamCodec() {
                return ParticleDarkSmokeData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleDarkSmokeData, IModBase> getClientComponent() {
        return new ParticleDarkSmokeConfigClientComponent();
    }
}
