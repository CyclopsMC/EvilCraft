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
 * Config for {@link ParticleColoredSmoke}.
 * @author rubensworks
 */
public class ParticleColoredSmokeConfig extends ParticleConfig<ParticleColoredSmokeData> {

    public ParticleColoredSmokeConfig() {
        super(EvilCraft._instance, "colored_smoke", eConfig -> new ParticleType<>(false, ParticleColoredSmokeData.DESERIALIZER));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleColoredSmokeData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleColoredSmokeData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleColoredSmokeData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleColoredSmoke particle = new ParticleColoredSmoke(worldIn, x, y, z,
                    particleData.getR(), particleData.getG(), particleData.getB(),
                    xSpeed, ySpeed, zSpeed, sprite);
            particle.selectSpriteRandomly(sprite);
            return particle;
        };
    }

}
