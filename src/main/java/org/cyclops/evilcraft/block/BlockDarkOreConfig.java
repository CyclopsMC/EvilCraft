package org.cyclops.evilcraft.block;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class BlockDarkOreConfig extends BlockConfig {

    public static ConfiguredFeature<?, ?> CONFIGURED_FEATURE;
    public static ConfiguredFeature<?, ?> CONFIGURED_FEATURE_BURRIED;

    public BlockDarkOreConfig() {
        super(
                EvilCraft._instance,
            "dark_ore",
                eConfig -> new BlockDarkOre(Block.Properties.of(Material.STONE)
                        .strength(3.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(getInstance(), RenderType.cutout());
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        // TODO: rewrite oregen (also add deepslate variant)
        /*CONFIGURED_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), getNamedId()),
                Feature.ORE
                        .configured(new OreConfiguration(OreFeatures.NATURAL_STONE, getInstance().defaultBlockState(), 4))
                        .range(66).squared().count(7));*/
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        // TODO: restore
        /*if (event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES)
                    .add(() -> CONFIGURED_FEATURE);
        }*/
    }
    
}
