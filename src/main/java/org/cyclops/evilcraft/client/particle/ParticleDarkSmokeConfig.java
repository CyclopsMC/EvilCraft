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
 * Config for {@link ParticleDarkSmoke}.
 * @author rubensworks
 */
public class ParticleDarkSmokeConfig extends ParticleConfig<ParticleDarkSmokeData> {

    public ParticleDarkSmokeConfig() {
        super(EvilCraft._instance, "dark_smoke", eConfig -> new ParticleType<ParticleDarkSmokeData>(false, ParticleDarkSmokeData.DESERIALIZER) {

            @Override
            public Codec<ParticleDarkSmokeData> codec() {
                return ParticleDarkSmokeData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleDarkSmokeData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleDarkSmokeData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleDarkSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleDarkSmoke particle = new ParticleDarkSmoke(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.isEntityDead());
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
