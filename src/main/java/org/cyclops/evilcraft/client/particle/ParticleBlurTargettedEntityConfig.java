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
 * Config for {@link ParticleBlurTargettedEntity}.
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityConfig extends ParticleConfigCommon<ParticleBlurTargettedEntityData, IModBase> {

    public ParticleBlurTargettedEntityConfig() {
        super(EvilCraft._instance, "blur_targetted_entity", eConfig -> new ParticleType<>(false) {

            @Override
            public MapCodec<ParticleBlurTargettedEntityData> codec() {
                return ParticleBlurTargettedEntityData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBlurTargettedEntityData> streamCodec() {
                return ParticleBlurTargettedEntityData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleBlurTargettedEntityData, IModBase> getClientComponent() {
        return new ParticleBlurTargettedEntityConfigClientComponent();
    }
}
