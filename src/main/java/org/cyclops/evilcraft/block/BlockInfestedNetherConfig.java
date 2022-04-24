package org.cyclops.evilcraft.block;

import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.GeneralConfig;

import java.util.List;

/**
 * Config for the {@link BlockInfestedNether}.
 * @author rubensworks
 *
 */
public class BlockInfestedNetherConfig extends BlockConfig {

    private final BlockInfestedNether.Type type;

    public static Holder<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE_NETHERFISH;
    public static Holder<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE_SILVERFISH_EXTRA;
    public static Holder<PlacedFeature> PLACED_FEATURE_NETHERFISH;
    public static Holder<PlacedFeature> PLACED_FEATURE_SILVERFISH_EXTRA;

    public BlockInfestedNetherConfig(BlockInfestedNether.Type type) {
        super(
            EvilCraft._instance,
            "infested_nether_" + type.name().toLowerCase(),
                eConfig -> new BlockInfestedNether(Block.Properties.of(Material.CLAY)
                        .strength(0.0F), type),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        this.type = type;
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        CONFIGURED_FEATURE_NETHERFISH = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), "stone_netherfish"),
                new ConfiguredFeature<>(Feature.ORE,
                        new OreConfiguration(OreFeatures.NETHER_ORE_REPLACEABLES, getInstance().defaultBlockState(), 9)));
        CONFIGURED_FEATURE_SILVERFISH_EXTRA = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), "stone_nether_silverfish_extra"),
                new ConfiguredFeature<>(Feature.ORE,
                        new OreConfiguration(OreFeatures.NATURAL_STONE, Blocks.INFESTED_STONE.defaultBlockState(), 4)));

        PLACED_FEATURE_NETHERFISH = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE,
                new ResourceLocation(getMod().getModId(), "stone_netherfish"),
                new PlacedFeature(CONFIGURED_FEATURE_NETHERFISH, List.of(
                        CountPlacement.of(10),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)),
                        BiomeFilter.biome())));
        PLACED_FEATURE_SILVERFISH_EXTRA = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE,
                new ResourceLocation(getMod().getModId(), "stone_nether_silverfish_extra"),
                new PlacedFeature(CONFIGURED_FEATURE_SILVERFISH_EXTRA, List.of(
                        CountPlacement.of(10),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(66)),
                        BiomeFilter.biome())));
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        // Only for type netherrack, as this event will be invoked for all infested block types
        if (this.type == BlockInfestedNether.Type.NETHERRACK && event.getCategory() == Biome.BiomeCategory.NETHER) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION)
                    .add(PLACED_FEATURE_NETHERFISH);
        }

        if (this.type != BlockInfestedNether.Type.NETHERRACK && GeneralConfig.extraSilverfish
                && event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION)
                    .add(PLACED_FEATURE_SILVERFISH_EXTRA);
        }
    }

}
