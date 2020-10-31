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
import org.cyclops.evilcraft.world.gen.decorator.WorldDecoratorEvilDungeon;

/**
 * Config for the {@link WorldDecoratorEvilDungeon}.
 * @author rubensworks
 *
 */
public class WorldFeatureEvilDungeonConfig extends WorldFeatureConfig {

    public WorldFeatureEvilDungeonConfig() {
        super(
                EvilCraft._instance,
                "evil_dungeon",
                eConfig -> new WorldFeatureEvilDungeon(NoFeatureConfig::deserialize)
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ((WorldFeatureEvilDungeon) getInstance())
                        .withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                        .withPlacement(RegistryEntries.DECORATOR_EVIL_DUNGEON.configure(new ChanceConfig(8))));
            }
        }
    }
}
