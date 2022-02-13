package org.cyclops.evilcraft.world.gen;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A Menril tree.
 * @author rubensworks
 */
public class TreeUndead extends Tree {

    public static BaseTreeFeatureConfig getTreeConfig() {
        return new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(RegistryEntries.BLOCK_UNDEAD_LOG.defaultBlockState()),
                new SimpleBlockStateProvider(RegistryEntries.BLOCK_UNDEAD_LEAVES.defaultBlockState()),
                new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
                new StraightTrunkPlacer(9, 4, 0),
                new TwoLayerFeature(1, 0, 1))
                .ignoreVines()
                .build();
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean b) {
        return Feature.TREE.configured(getTreeConfig());
    }

}
