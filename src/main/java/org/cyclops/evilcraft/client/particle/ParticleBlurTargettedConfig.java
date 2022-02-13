package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleBlurTargetted}.
 * @author rubensworks
 */
public class ParticleBlurTargettedConfig extends ParticleConfig<ParticleBlurTargettedData> {

    public ParticleBlurTargettedConfig() {
        super(EvilCraft._instance, "blur_targetted", eConfig -> new ParticleType<ParticleBlurTargettedData>(false, ParticleBlurTargettedData.DESERIALIZER) {

            @Override
            public Codec<ParticleBlurTargettedData> codec() {
                return ParticleBlurTargettedData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleBlurTargettedData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleBlurTargettedData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleBlurTargettedData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleBlurTargetted particle = new ParticleBlurTargetted(particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
