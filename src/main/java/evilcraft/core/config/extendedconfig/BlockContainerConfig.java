package evilcraft.core.config.extendedconfig;

import com.google.common.collect.Sets;
import evilcraft.core.config.ConfigurableType;
import evilcraft.core.tileentity.upgrade.Upgrades;
import net.minecraft.block.Block;

import java.util.Set;

/**
 * Config for blocks with tile entities.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class BlockContainerConfig extends BlockConfig {

	/**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
	public BlockContainerConfig(boolean enabled, String namedId,
			String comment, Class<? extends Block> element) {
		super(enabled, namedId, comment, element);
	}
	
	@Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BLOCKCONTAINER;
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
        for(Upgrades.Upgrade upgrade : getUpgrades()) {
            upgrade.addUpgradableInfo(this);
        }
    }

}
