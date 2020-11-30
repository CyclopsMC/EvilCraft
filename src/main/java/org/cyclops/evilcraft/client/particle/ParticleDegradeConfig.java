package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleDegrade}.
 * @author rubensworks
 */
public class ParticleDegradeConfig extends ParticleConfig<BasicParticleType> {

    public ParticleDegradeConfig() {
        super(EvilCraft._instance, "degrade", eConfig -> new BasicParticleType(false));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<BasicParticleType> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<BasicParticleType> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<BasicParticleType>) (typeIn, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleDegrade particle = new ParticleDegrade(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.selectSpriteRandomly(sprite);
            return particle;
        };
    }

}
