package org.cyclops.evilcraft.world.gen.feature;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.WorldFeatureConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link WorldFeatureStructureDarkTemple}.
 * @author rubensworks
 *
 */
public class WorldFeatureStructureDarkTempleConfig extends WorldFeatureConfig {

    @ConfigurableProperty(category = "worldgeneration", comment = "Minimum block height at which a dark temple can spawn.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleMinHeight = 64;
    @ConfigurableProperty(category = "worldgeneration", comment = "Maximum blockState height at which a dark temple can spawn.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleMaxHeight = 90;
    @ConfigurableProperty(category = "worldgeneration", comment = "Average distance between dark temples in chunks.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleSpacing = 40;
    @ConfigurableProperty(category = "worldgeneration", comment = "Minimum distance between dark temples in chunks, must be smaller than spacing.", configLocation = ModConfig.Type.SERVER)
    public static int darkTempleSeparation = 16;

    public WorldFeatureStructureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> new WorldFeatureStructureDarkTemple(NoFeatureConfig::deserialize)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for (Biome biome : ForgeRegistries.BIOMES) {
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ((WorldFeatureStructureDarkTemple) getInstance())
                    .withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                    .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            biome.addStructure(((WorldFeatureStructureDarkTemple) getInstance())
                    .withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }
    }
}
