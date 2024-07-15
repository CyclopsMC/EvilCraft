package org.cyclops.evilcraft.client.particle;

import com.mojang.datafixers.util.Function8;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.function.Function;

/**
 * @author rubensworks
 */
public class ParticleBlurTargettedData extends ParticleBlurData {

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleBlurTargettedData> STREAM_CODEC = composite(
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getRed,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getGreen,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getBlue,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getScale,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getAgeMultiplier,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getTargetX,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getTargetY,
            ByteBufCodecs.FLOAT, ParticleBlurTargettedData::getTargetZ,
            ParticleBlurTargettedData::new
    );
    public static final MapCodec<ParticleBlurTargettedData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(
                    Codec.FLOAT.fieldOf("r").forGetter(ParticleBlurTargettedData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleBlurTargettedData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleBlurTargettedData::getBlue),
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleBlurTargettedData::getScale),
                    Codec.FLOAT.fieldOf("age").forGetter(ParticleBlurTargettedData::getAgeMultiplier),
                    Codec.FLOAT.fieldOf("x").forGetter(ParticleBlurTargettedData::getTargetX),
                    Codec.FLOAT.fieldOf("y").forGetter(ParticleBlurTargettedData::getTargetY),
                    Codec.FLOAT.fieldOf("z").forGetter(ParticleBlurTargettedData::getTargetZ)
            )
            .apply(builder, ParticleBlurTargettedData::new));

    private final float targetX;
    private final float targetY;
    private final float targetZ;

    public ParticleBlurTargettedData(float red, float green, float blue, float scale, float ageMultiplier, float targetX, float targetY, float targetZ) {
        super(red, green, blue, scale, ageMultiplier);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_BLUR_TARGETTED.get();
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> p_331335_) {
        return new StreamCodec<>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
            }
        };
    }
}
