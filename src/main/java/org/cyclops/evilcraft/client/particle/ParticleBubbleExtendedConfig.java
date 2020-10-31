package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleBubbleExtended}.
 * @author rubensworks
 */
public class ParticleBubbleExtendedConfig extends ParticleConfig<ParticleBubbleExtendedData> {

    public ParticleBubbleExtendedConfig() {
        super(EvilCraft._instance, "bubble_extended", eConfig -> new ParticleType<>(false, ParticleBubbleExtendedData.DESERIALIZER));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleBubbleExtendedData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleBubbleExtendedData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleBubbleExtendedData>) (particleData, world, x, y, z, motionX, motionY, motionZ) -> {
            ParticleBubbleExtended particle = new ParticleBubbleExtended(world, x, y, z, motionX, motionY, motionZ, particleData.getGravity());
            particle.selectSpriteRandomly(sprite);
            return particle;
        };
    }

}
