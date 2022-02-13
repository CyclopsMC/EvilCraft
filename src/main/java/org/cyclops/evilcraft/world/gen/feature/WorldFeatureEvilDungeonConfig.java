package org.cyclops.evilcraft.world.gen.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.cyclops.cyclopscore.config.extendedconfig.WorldFeatureConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.world.gen.structure.WorldStructureDarkTemple;

/**
 * Config for the evil dungeon.
 * @author rubensworks
 *
 */
public class WorldFeatureEvilDungeonConfig extends WorldFeatureConfig {

    public static ConfiguredFeature<?, ?> CONFIGURED_FEATURE;

    public WorldFeatureEvilDungeonConfig() {
        super(
                EvilCraft._instance,
                "evil_dungeon",
                eConfig -> new WorldFeatureEvilDungeon(NoFeatureConfig.CODEC)
        );
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        CONFIGURED_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(getMod().getModId(), getNamedId() + "_default"),
                ((WorldFeatureEvilDungeon) getInstance())
                        .configured(IFeatureConfig.NONE)
                        .range(256).squared().count(10));
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NETHER) {
            event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_STRUCTURES)
                    .add(() -> CONFIGURED_FEATURE);
        }
    }
}
