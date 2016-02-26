package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileWorking;

import java.util.Set;

/**
 * Config for the {@link SanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class SanguinaryEnvironmentalAccumulatorConfig extends UpgradableBlockContainerConfig {

    /**
     * The unique instance.
     */
    public static SanguinaryEnvironmentalAccumulatorConfig _instance;

    /**
     * The base blood usage in mB for recipes, this is multiplied with the cooldown time per recipe.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, isCommandable = true, comment = "The base blood usage in mB for recipes, this is multiplied with the cooldown time per recipe.")
    public static int baseUsage = 50;

    /**
     * Make a new instance.
     */
    public SanguinaryEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
        	true,
            "sanguinaryEnvironmentalAccumulator",
            null,
            SanguinaryEnvironmentalAccumulator.class
        );
    }

    @Override
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet(
                TileWorking.UPGRADE_EFFICIENCY,
                TileWorking.UPGRADE_SPEED,
                TileWorking.UPGRADE_TIER1,
                TileWorking.UPGRADE_TIER2,
                TileWorking.UPGRADE_TIER3);
    }

    @Override
    protected String getConfigPropertyPrefix() {
        return "sangEnvirAcc";
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(!Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            throw new RuntimeException("Enabling the Sanguinary Environmental Accumulator requires the regular " +
                    "Environmental Accumulator to be enabled!");
        }
    }
}
