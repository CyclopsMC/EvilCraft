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
 * Config for {@link ParticleBloodBrick}.
 * @author rubensworks
 */
public class ParticleBloodBrickConfig extends ParticleConfig<ParticleBloodBrickData> {

    public ParticleBloodBrickConfig() {
        super(EvilCraft._instance, "blood_brick", eConfig -> new ParticleType<>(false, ParticleBloodBrickData.DESERIALIZER));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleBloodBrickData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleBloodBrickData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleBloodBrickData>) (particleData, world, x, y, z, motionX, motionY, motionZ) -> {
            ParticleBloodBrick particle = new ParticleBloodBrick(world, x, y, z, particleData.getSide());
            return particle;
        };
    }

}
