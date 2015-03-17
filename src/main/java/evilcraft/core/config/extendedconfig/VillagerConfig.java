package evilcraft.core.config.extendedconfig;

import evilcraft.core.config.ConfigurableType;
import evilcraft.core.config.configurable.ConfigurableVillager;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class VillagerConfig extends ExtendedConfig<VillagerConfig> {
	
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
    }
    
    @Override
	public String getUnlocalizedName() {
		return "entity.villager." + getNamedId();
	}
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.VILLAGER;
	}

}
