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
 * Config for {@link ParticleBlurTargettedEntity}.
 * @author rubensworks
 */
public class ParticleBlurTargettedEntityConfig extends ParticleConfig<ParticleBlurTargettedEntityData> {

    public ParticleBlurTargettedEntityConfig() {
        super(EvilCraft._instance, "blur_targetted_entity", eConfig -> new ParticleType<ParticleBlurTargettedEntityData>(false, ParticleBlurTargettedEntityData.DESERIALIZER) {

            @Override
            public Codec<ParticleBlurTargettedEntityData> codec() {
                return ParticleBlurTargettedEntityData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleBlurTargettedEntityData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleBlurTargettedEntityData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleBlurTargettedEntityData>) (particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            ParticleBlurTargettedEntity particle = new ParticleBlurTargettedEntity(particleData, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite);
            return particle;
        };
    }

}
