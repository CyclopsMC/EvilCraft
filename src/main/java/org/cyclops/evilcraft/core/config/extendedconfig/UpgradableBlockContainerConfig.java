package org.cyclops.evilcraft.core.config.extendedconfig;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;

import java.util.Set;

/**
 * Config for upgradable blocks with tile entities.
 * @author rubensworks
 */
public class UpgradableBlockContainerConfig extends BlockContainerConfig {

    /**
     * Make a new instance.
     *
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public UpgradableBlockContainerConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends Block> element) {
        super(mod, enabled, namedId, comment, element);
    }

    /**
     * @return The set of upgrades that can be applied to this machine.
     */
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet();
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        for (Upgrades.Upgrade upgrade : getUpgrades()) {
            upgrade.addUpgradableInfo(this);
        }
    }
}
