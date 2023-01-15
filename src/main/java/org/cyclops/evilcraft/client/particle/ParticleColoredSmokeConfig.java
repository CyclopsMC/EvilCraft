package org.cyclops.evilcraft.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleColoredSmoke}.
 * @author rubensworks
 */
public class ParticleColoredSmokeConfig extends ParticleConfig<ParticleColoredSmokeData> {

    public ParticleColoredSmokeConfig() {
        super(EvilCraft._instance, "colored_smoke", eConfig -> new ParticleType<ParticleColoredSmokeData>(false, ParticleColoredSmokeData.DESERIALIZER) {

            @Override
            public Codec<ParticleColoredSmokeData> codec() {
                return ParticleColoredSmokeData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleColoredSmokeData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleColoredSmokeData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleColoredSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleColoredSmoke particle = new ParticleColoredSmoke(worldIn, x, y, z,
                    particleData.getR(), particleData.getG(), particleData.getB(),
                    xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
