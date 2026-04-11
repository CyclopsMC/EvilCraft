package org.cyclops.evilcraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class DataComponentBiomeConfig extends DataComponentConfigCommon<DataComponentBiomeConfig.BiomeHolder, IModBase> {
    public DataComponentBiomeConfig() {
        super(EvilCraft._instance, "biome", builder -> builder
                .persistent(BiomeHolder.CODEC)
                .networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(BiomeHolder.CODEC)));
    }

    public static record BiomeHolder(Identifier id, HolderGetter<Biome> getter) {
        public static Codec<BiomeHolder> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                                Identifier.CODEC.fieldOf("name").forGetter(BiomeHolder::id),
                                RegistryOps.retrieveGetter(Registries.BIOME)
                        )
                        .apply(builder, BiomeHolder::new)
        );

        @Nullable
        public Holder<Biome> getBiome() {
            return getter.get(ResourceKey.create(Registries.BIOME, id)).orElse(null);
        }
    }
}
