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
 * Config for {@link ParticleColoredSmoke}.
 * @author rubensworks
 */
public class ParticleColoredSmokeConfig extends ParticleConfigCommon<ParticleColoredSmokeData, IModBase> {

    public ParticleColoredSmokeConfig() {
        super(EvilCraft._instance, "colored_smoke", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleColoredSmokeData> codec() {
                return ParticleColoredSmokeData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleColoredSmokeData> streamCodec() {
                return ParticleColoredSmokeData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleColoredSmokeData, IModBase> getClientComponent() {
        return new ParticleColoredSmokeConfigClientComponent();
    }
}
