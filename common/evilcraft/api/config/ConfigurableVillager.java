package evilcraft.api.config;


public class ConfigurableVillager implements Configurable {

    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.VILLAGER;
    
    protected ConfigurableVillager(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        
    }
    
    // Set a configuration for this villager
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public String getUniqueName() {
        return "villager."+eConfig.NAMEDID;
    }
    
    public boolean isEntity() {
        return true;
    }

}
