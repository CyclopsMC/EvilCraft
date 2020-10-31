package org.cyclops.evilcraft.world.gen;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A Menril tree.
 * @author rubensworks
 */
public class TreeUndead extends Tree {

    public static TreeFeatureConfig getMenrilTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(RegistryEntries.BLOCK_UNDEAD_LOG.getDefaultState()),
                new SimpleBlockStateProvider(RegistryEntries.BLOCK_UNDEAD_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(2, 0))
                .baseHeight(9)
                .heightRandA(4)
                .foliageHeight(3)
                .ignoreVines()
                .setSapling((net.minecraftforge.common.IPlantable) RegistryEntries.BLOCK_UNDEAD_SAPLING)
                .build();
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean b) {
        return new TreeFeature(TreeFeatureConfig::func_227338_a_).withConfiguration(getMenrilTreeConfig());
    }

}
