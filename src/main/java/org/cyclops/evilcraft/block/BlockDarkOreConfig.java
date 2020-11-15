package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class BlockDarkOreConfig extends BlockConfig {

    @ConfigurableProperty(category = "worldgeneration", comment = "How much ores per vein.")
    public static int blocksPerVein = 4;

    @ConfigurableProperty(category = "worldgeneration", comment = "How many veins per chunk.")
    public static int veinsPerChunk = 7;

    @ConfigurableProperty(category = "worldgeneration", comment = "Generation ends of this level.")
    public static int endY = 66;

    public BlockDarkOreConfig() {
        super(
                EvilCraft._instance,
            "dark_ore",
                eConfig -> new BlockDarkOre(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(3.0F)
                        .sound(SoundType.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getCutout());
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, getInstance().getDefaultState(), blocksPerVein))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(veinsPerChunk, 0, 0, endY))));
            }
        }
    }
    
}
