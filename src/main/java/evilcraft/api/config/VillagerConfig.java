package evilcraft.api.config;

import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.config.configurable.ConfigurableVillager;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class VillagerConfig extends ExtendedConfig<VillagerConfig> {

	/**
	 * ID of this {@link Configurable}.
	 */
	public int ID;
	
    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public VillagerConfig(int defaultId, String namedId,
            String comment, Class<? extends ConfigurableVillager> element) {
        super(defaultId != 0, namedId, comment, element);
        this.ID = defaultId;
    }
    

}
