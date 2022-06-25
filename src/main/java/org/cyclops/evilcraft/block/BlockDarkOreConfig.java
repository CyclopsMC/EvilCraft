package org.cyclops.evilcraft.block;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.List;

/**
 * Config for the {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class BlockDarkOreConfig extends BlockConfig {

    private final boolean deepslate;
    public Holder<ConfiguredFeature<?, ?>> configuredFeature;
    public Holder<PlacedFeature> placedFeature;

    public BlockDarkOreConfig(boolean deepslate) {
        super(
                EvilCraft._instance,
            "dark_ore" + (deepslate ? "_deepslate" : ""),
                eConfig -> new BlockDarkOre(Block.Properties.of(Material.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(3.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        this.deepslate = deepslate;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.cutout());
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        String id = getNamedId() + (deepslate ? "_lower" : "_upper");
        configuredFeature = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), id),
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
                        OreConfiguration.target(deepslate ? OreFeatures.DEEPSLATE_ORE_REPLACEABLES : OreFeatures.STONE_ORE_REPLACEABLES, getInstance().defaultBlockState())
                ), 4)));

        placedFeature = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE,
                new ResourceLocation(getMod().getModId(), id),
                deepslate
                        ? new PlacedFeature(configuredFeature, List.of(CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32)), BiomeFilter.biome()))
                        : new PlacedFeature(configuredFeature, List.of(CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(66)), BiomeFilter.biome())));
    }

}
