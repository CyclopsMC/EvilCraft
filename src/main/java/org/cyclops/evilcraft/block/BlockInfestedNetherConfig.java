package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.GeneralConfig;

/**
 * Config for the {@link BlockInfestedNether}.
 * @author rubensworks
 *
 */
public class BlockInfestedNetherConfig extends BlockConfig {

    @ConfigurableProperty(category = "worldgeneration", comment = "How many veins per chunk.")
    public static int veinsPerChunk = 10;

    private final BlockInfestedNether.Type type;

    public static ConfiguredFeature<?, ?> CONFIGURED_FEATURE_SILVERFISH_EXTRA;

    public BlockInfestedNetherConfig(BlockInfestedNether.Type type) {
        super(
            EvilCraft._instance,
            "infested_nether_" + type.name().toLowerCase(),
                eConfig -> new BlockInfestedNether(Block.Properties.create(Material.CLAY)
                        .hardnessAndResistance(0.0F), type),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        this.type = type;
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        CONFIGURED_FEATURE_SILVERFISH_EXTRA = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), "stone_netherfish"),
                Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, getInstance().getDefaultState(), 9))
                        .range(64).square().func_242731_b(veinsPerChunk));
        CONFIGURED_FEATURE_SILVERFISH_EXTRA = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), "stone_nether_silverfish_extra"),
                Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Blocks.INFESTED_STONE.getDefaultState(), GeneralConfig.silverfishBlocksPerVein))
                        .range(GeneralConfig.silverfishEndY).square().func_242731_b(GeneralConfig.silverfishVeinsPerChunk));
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.Category.NETHER) {
            event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION)
                    .add(() -> CONFIGURED_FEATURE_SILVERFISH_EXTRA);
        }

        // Only for type netherrack, as this event will be invoked for all infested block typess
        if (this.type == BlockInfestedNether.Type.NETHERRACK && GeneralConfig.extraSilverfish
                && event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NETHER) {
            event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION)
                    .add(() -> CONFIGURED_FEATURE_SILVERFISH_EXTRA);
        }
    }
    
}
