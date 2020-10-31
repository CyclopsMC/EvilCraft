package org.cyclops.evilcraft.world.gen.feature;

import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.cyclops.cyclopscore.config.extendedconfig.WorldFeatureConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the undead tree.
 * @author rubensworks
 *
 */
public class WorldFeatureTreeUndeadConfig extends WorldFeatureConfig {
    public WorldFeatureTreeUndeadConfig() {
        super(
                EvilCraft._instance,
                "tree_undead",
                eConfig -> new TreeFeature(TreeFeatureConfig::func_227338_a_)
        );
    }
}
