package evilcraft.entities.villager;

import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableVillager;

public class WerewolfVillager extends ConfigurableVillager{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.VILLAGER;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public WerewolfVillager(ExtendedConfig eConfig) {
        super(eConfig);
    }
}
