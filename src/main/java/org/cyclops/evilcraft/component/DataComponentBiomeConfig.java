package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class DataComponentBiomeConfig extends DataComponentConfig<DataComponentBiomeConfig.BiomeHolder> {
    public DataComponentBiomeConfig() {
        super(EvilCraft._instance, "biome", builder -> builder
                .persistent(BiomeHolder.CODEC)
                .networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(BiomeHolder.CODEC)));
    }

    public static record BiomeHolder(ResourceLocation id, HolderGetter<Biome> getter) {
        public static Codec<BiomeHolder> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                                ResourceLocation.CODEC.fieldOf("name").forGetter(BiomeHolder::id),
                                RegistryOps.retrieveGetter(Registries.BIOME)
                        )
                        .apply(builder, BiomeHolder::new)
        );

        public Holder<Biome> getBiome() {
            return getter.getOrThrow(ResourceKey.create(Registries.BIOME, id));
        }
    }
}
