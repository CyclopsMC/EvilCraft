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
 * Config for {@link ParticleFart}.
 * @author rubensworks
 */
public class ParticleFartConfig extends ParticleConfig<ParticleFartData> {

    public ParticleFartConfig() {
        super(EvilCraft._instance, "fart", eConfig -> new ParticleType<ParticleFartData>(false, ParticleFartData.DESERIALIZER) {

            @Override
            public Codec<ParticleFartData> codec() {
                return ParticleFartData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleFartData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleFartData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleFartData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleFart particle = new ParticleFart(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.getRainbow());
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
