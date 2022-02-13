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
                    DefaultBiomeFeatures.desertSpawns(mobSpawnsBuilder);
                    BiomeGenerationSettings.Builder generationBuilder = (new BiomeGenerationSettings.Builder())
                            .surfaceBuilder(ConfiguredSurfaceBuilders.DESERT);

                    DefaultBiomeFeatures.addDefaultOverworldLandStructures(generationBuilder);
                    generationBuilder.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);
                    DefaultBiomeFeatures.addDefaultCarvers(generationBuilder);
                    DefaultBiomeFeatures.addDesertLakes(generationBuilder);
                    DefaultBiomeFeatures.addDefaultMonsterRoom(generationBuilder);
                    DefaultBiomeFeatures.addDefaultUndergroundVariety(generationBuilder);
                    DefaultBiomeFeatures.addDefaultOres(generationBuilder);
                    DefaultBiomeFeatures.addDefaultSoftDisks(generationBuilder);
                    //DefaultBiomeFeatures.withDefaultFlowers(generationBuilder);
                    //DefaultBiomeFeatures.withBadlandsGrass(generationBuilder);
                    DefaultBiomeFeatures.addDesertVegetation(generationBuilder);
                    DefaultBiomeFeatures.addDefaultMushrooms(generationBuilder);
                    //DefaultBiomeFeatures.withDesertVegetation(generationBuilder);
                    DefaultBiomeFeatures.addDefaultSprings(generationBuilder);
                    //DefaultBiomeFeatures.withDesertWells(generationBuilder);
                    DefaultBiomeFeatures.addSurfaceFreezing(generationBuilder);

                    DefaultBiomeFeatures.addForestGrass(generationBuilder);

                    return new Biome.Builder()
                            .precipitation(Biome.RainType.NONE)
                            .biomeCategory(Biome.Category.THEEND)
                            .depth(0.125F)
                            .scale(0.4F)
                            .temperature(0.8F)
                            .downfall(0.9F)
                            .specialEffects((new BiomeAmbience.Builder())
                                    .waterColor(Helpers.RGBToInt(60, 50, 20))
                                    .waterFogColor(Helpers.RGBToInt(60, 50, 20))
                                    .fogColor(Helpers.RGBToInt(60, 50, 20))
                                    .skyColor(Helpers.RGBToInt(10, 20, 5))
                                    .grassColorOverride(Helpers.RGBToInt(10, 20, 5))
                                    .foliageColorOverride(Helpers.RGBToInt(10, 20, 50))
                                    .ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
                            .mobSpawnSettings(mobSpawnsBuilder.build())
                            .generationSettings(generationBuilder.build())
                            .build();
                }
        );
    }
    
}
