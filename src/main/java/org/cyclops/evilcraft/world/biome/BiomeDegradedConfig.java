package org.cyclops.evilcraft.world.biome;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
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
                    MobSpawnSettings.Builder mobSpawnsBuilder = new MobSpawnSettings.Builder();
                    BiomeDefaultFeatures.desertSpawns(mobSpawnsBuilder);
                    BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder();

                    //OverworldBiomes.globalOverworldGeneration(biomegenerationsettings$builder);
                    BiomeDefaultFeatures.addDefaultCarversAndLakes(generationBuilder);
                    BiomeDefaultFeatures.addDefaultMonsterRoom(generationBuilder);
                    BiomeDefaultFeatures.addDefaultUndergroundVariety(generationBuilder);
                    BiomeDefaultFeatures.addDefaultOres(generationBuilder);
                    BiomeDefaultFeatures.addDefaultSoftDisks(generationBuilder);
                    //DefaultBiomeFeatures.withDefaultFlowers(generationBuilder);
                    //DefaultBiomeFeatures.withBadlandsGrass(generationBuilder);
                    BiomeDefaultFeatures.addDesertVegetation(generationBuilder);
                    BiomeDefaultFeatures.addDefaultMushrooms(generationBuilder);
                    //DefaultBiomeFeatures.withDesertVegetation(generationBuilder);
                    BiomeDefaultFeatures.addDefaultSprings(generationBuilder);
                    //DefaultBiomeFeatures.withDesertWells(generationBuilder);
                    BiomeDefaultFeatures.addSurfaceFreezing(generationBuilder);

                    BiomeDefaultFeatures.addForestGrass(generationBuilder);

                    return new Biome.BiomeBuilder()
                            .precipitation(Biome.Precipitation.NONE)
                            .temperature(0.8F)
                            .downfall(0.9F)
                            .specialEffects((new BiomeSpecialEffects.Builder())
                                    .waterColor(Helpers.RGBToInt(60, 50, 20))
                                    .waterFogColor(Helpers.RGBToInt(60, 50, 20))
                                    .fogColor(Helpers.RGBToInt(60, 50, 20))
                                    .skyColor(Helpers.RGBToInt(10, 20, 5))
                                    .grassColorOverride(Helpers.RGBToInt(10, 20, 5))
                                    .foliageColorOverride(Helpers.RGBToInt(10, 20, 50))
                                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
                            .mobSpawnSettings(mobSpawnsBuilder.build())
                            .generationSettings(generationBuilder.build())
                            .build();
                }
        );
    }

}
