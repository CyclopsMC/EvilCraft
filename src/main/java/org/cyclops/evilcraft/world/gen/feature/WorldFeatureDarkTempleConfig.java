package org.cyclops.evilcraft.world.gen.feature;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.config.extendedconfig.WorldFeatureConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link WorldFeatureDarkTemple}.
 * @author rubensworks
 *
 */
public class WorldFeatureDarkTempleConfig extends WorldFeatureConfig {

    public WorldFeatureDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> new WorldFeatureDarkTemple(NoFeatureConfig::deserialize)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ((WorldFeatureDarkTemple) getInstance())
                        .withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                        .withPlacement(RegistryEntries.DECORATOR_DARK_TEMPLE.configure(new ChanceConfig(8))));
            }
        }
    }
}
