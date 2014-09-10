package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;

/**
 * Dummy config.
 * @author rubensworks
 * @see ExtendedConfig
 *
 */
public class DummyConfig extends ExtendedConfig<DummyConfig>{

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public DummyConfig(boolean enabled, String namedId,
            String comment, Class<?> element) {
        super(enabled, namedId, comment, element);
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.DUMMY;
	}

	@Override
	public String getUnlocalizedName() {
		return getNamedId();
	}

}
