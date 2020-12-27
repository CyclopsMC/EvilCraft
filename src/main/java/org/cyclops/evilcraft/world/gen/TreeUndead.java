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
                new SimpleBlockStateProvider(RegistryEntries.BLOCK_UNDEAD_LOG.getDefaultState()),
                new SimpleBlockStateProvider(RegistryEntries.BLOCK_UNDEAD_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
                new StraightTrunkPlacer(9, 4, 0),
                new TwoLayerFeature(1, 0, 1))
                .setIgnoreVines()
                .build();
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean b) {
        return Feature.TREE.withConfiguration(getTreeConfig());
    }

}
