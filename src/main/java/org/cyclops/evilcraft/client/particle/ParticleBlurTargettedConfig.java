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
 * Config for {@link ParticleBlurTargetted}.
 * @author rubensworks
 */
public class ParticleBlurTargettedConfig extends ParticleConfigCommon<ParticleBlurTargettedData, IModBase> {

    public ParticleBlurTargettedConfig() {
        super(EvilCraft._instance, "blur_targetted", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleBlurTargettedData> codec() {
                return ParticleBlurTargettedData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBlurTargettedData> streamCodec() {
                return ParticleBlurTargettedData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleBlurTargettedData, IModBase> getClientComponent() {
        return new ParticleBlurTargettedConfigClientComponent();
    }
}
