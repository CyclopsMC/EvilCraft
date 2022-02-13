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
 * Config for {@link ParticleMagicFinish}.
 * @author rubensworks
 */
public class ParticleMagicFinishConfig extends ParticleConfig<BasicParticleType> {

    public ParticleMagicFinishConfig() {
        super(EvilCraft._instance, "magic_finish", eConfig -> new BasicParticleType(false));
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
            ParticleMagicFinish particle = new ParticleMagicFinish(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
