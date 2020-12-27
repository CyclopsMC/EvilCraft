package org.cyclops.evilcraft.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the degraded biome.
 * @author rubensworks
 *
 */
public class BiomeDegradedConfig extends BiomeConfig {

    public BiomeDegradedConfig() {
        super(
                EvilCraft._instance,
                "degraded",
                eConfig -> {
                    MobSpawnInfo.Builder mobSpawnsBuilder = new MobSpawnInfo.Builder();
                    DefaultBiomeFeatures.withDesertMobs(mobSpawnsBuilder);
                    BiomeGenerationSettings.Builder generationBuilder = (new BiomeGenerationSettings.Builder())
                            .withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244172_d);

                    DefaultBiomeFeatures.withStrongholdAndMineshaft(generationBuilder);
                    generationBuilder.withStructure(StructureFeatures.RUINED_PORTAL_DESERT);
                    DefaultBiomeFeatures.withCavesAndCanyons(generationBuilder);
                    DefaultBiomeFeatures.withLavaLakes(generationBuilder);
                    DefaultBiomeFeatures.withMonsterRoom(generationBuilder);
                    DefaultBiomeFeatures.withCommonOverworldBlocks(generationBuilder);
                    DefaultBiomeFeatures.withOverworldOres(generationBuilder);
                    DefaultBiomeFeatures.withDisks(generationBuilder);
                    //DefaultBiomeFeatures.withDefaultFlowers(generationBuilder);
                    //DefaultBiomeFeatures.withBadlandsGrass(generationBuilder);
                    DefaultBiomeFeatures.withDesertDeadBushes(generationBuilder);
                    DefaultBiomeFeatures.withNormalMushroomGeneration(generationBuilder);
                    //DefaultBiomeFeatures.withDesertVegetation(generationBuilder);
                    DefaultBiomeFeatures.withLavaAndWaterSprings(generationBuilder);
                    //DefaultBiomeFeatures.withDesertWells(generationBuilder);
                    DefaultBiomeFeatures.withFrozenTopLayer(generationBuilder);

                    DefaultBiomeFeatures.withForestGrass(generationBuilder);

                    return new Biome.Builder()
                            .precipitation(Biome.RainType.NONE)
                            .category(Biome.Category.THEEND)
                            .depth(0.125F)
                            .scale(0.4F)
                            .temperature(0.8F)
                            .downfall(0.9F)
                            .setEffects((new BiomeAmbience.Builder())
                                    .setWaterColor(Helpers.RGBToInt(60, 50, 20))
                                    .setWaterFogColor(Helpers.RGBToInt(60, 50, 20))
                                    .setFogColor(Helpers.RGBToInt(60, 50, 20))
                                    .withSkyColor(Helpers.RGBToInt(10, 20, 5))
                                    .withGrassColor(Helpers.RGBToInt(10, 20, 5))
                                    .withFoliageColor(Helpers.RGBToInt(10, 20, 50))
                                    .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                            .withMobSpawnSettings(mobSpawnsBuilder.copy())
                            .withGenerationSettings(generationBuilder.build())
                            .build();
                }
        );
    }
    
}
