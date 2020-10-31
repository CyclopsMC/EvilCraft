package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleType;
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
        super(EvilCraft._instance, "fart", eConfig -> new ParticleType<>(false, ParticleFartData.DESERIALIZER));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleFartData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleFartData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleFartData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleFart particle = new ParticleFart(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, particleData.getRainbow(), sprite);
            particle.selectSpriteRandomly(sprite);
            return particle;
        };
    }

}
