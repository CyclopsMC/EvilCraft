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
 * Config for {@link ParticleBlurTargettedEntity}.
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityConfig extends ParticleConfig<ParticleBlurTargettedEntityData> {

    public ParticleBlurTargettedEntityConfig() {
        super(EvilCraft._instance, "blur_targetted_entity", eConfig -> new ParticleType<ParticleBlurTargettedEntityData>(false, ParticleBlurTargettedEntityData.DESERIALIZER) {

            @Override
            public Codec<ParticleBlurTargettedEntityData> func_230522_e_() {
                return ParticleBlurTargettedEntityData.CODEC;
            }
        });
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
