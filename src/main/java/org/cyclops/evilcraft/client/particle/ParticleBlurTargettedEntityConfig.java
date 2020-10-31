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
 * Config for {@link ParticleBlurTargettedEntity}.
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityConfig extends ParticleConfig<ParticleBlurTargettedEntityData> {

    public ParticleBlurTargettedEntityConfig() {
        super(EvilCraft._instance, "blur_targetted_entity", eConfig -> new ParticleType<>(false, ParticleBlurTargettedEntityData.DESERIALIZER));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleBlurTargettedEntityData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleBlurTargettedEntityData> getParticleMetaFactory() {
        return sprite -> (IParticleFactory<ParticleBlurTargettedEntityData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleBlurTargettedEntity particle = new ParticleBlurTargettedEntity(particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.selectSpriteRandomly(sprite);
            return particle;
        };
    }

}
