package org.cyclops.evilcraft.world.gen;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockUndeadSaplingConfig;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * An undead tree.
 * @author rubensworks
 */
public class TreeUndead extends AbstractTreeGrower {

    public static TreeConfiguration getTreeConfig() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(RegistryEntries.BLOCK_UNDEAD_LOG.defaultBlockState()),
                new StraightTrunkPlacer(9, 4, 0),
                BlockStateProvider.simple(RegistryEntries.BLOCK_UNDEAD_LEAVES.defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1))
                .ignoreVines()
                .build();
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean b) {
        return BlockUndeadSaplingConfig.CONFIGURED_FEATURE_TREE;
    }

}
