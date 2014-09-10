package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;
import net.minecraft.block.Block;

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

}
