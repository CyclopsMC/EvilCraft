package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
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
    public static int veinsPerChunk = 250;

    public BlockInfestedNetherConfig(BlockInfestedNether.Type type) {
        super(
            EvilCraft._instance,
            "infested_nether_" + type.name().toLowerCase(),
                eConfig -> new BlockInfestedNether(Block.Properties.create(Material.CLAY)
                        .hardnessAndResistance(0.0F), type),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome.getCategory() == Biome.Category.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, getInstance().getDefaultState(), 9))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(veinsPerChunk, 0, 0, 64))));
            }
        }

        if(GeneralConfig.extraSilverfish && GeneralConfig.silverfish_BlocksPerVein > 0 && GeneralConfig.silverfish_VeinsPerChunk > 0) {
            for (Biome biome : ForgeRegistries.BIOMES) {
                if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE
                            .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.INFESTED_STONE.getDefaultState(), GeneralConfig.silverfish_BlocksPerVein))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(GeneralConfig.silverfish_VeinsPerChunk, 0, 0, GeneralConfig.silverfish_EndY))));
                }
            }
        }
    }
    
}
