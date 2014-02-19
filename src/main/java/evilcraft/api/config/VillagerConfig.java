package evilcraft.api.config;

import evilcraft.api.config.configurable.ConfigurableVillager;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class VillagerConfig extends ExtendedConfig<VillagerConfig> {

    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param name The name for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public VillagerConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends ConfigurableVillager> element) {
        super(defaultId, name, namedId, comment, element);
    }
    

}
