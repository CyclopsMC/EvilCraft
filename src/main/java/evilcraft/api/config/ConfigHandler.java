package evilcraft.api.config;

import java.util.LinkedHashSet;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import evilcraft.EvilCraft;
import evilcraft.commands.CommandConfig;
import evilcraft.commands.CommandConfigSet;

/**
 * Create config file and register items & blocks from the given ExtendedConfigs
 * @author rubensworks
 *
 */
@SuppressWarnings("rawtypes")
public class ConfigHandler extends LinkedHashSet<ExtendedConfig>{
    
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;
    
    private static ConfigHandler _instance = null;
    
    /**
     * Get the unique instance.
     * @return Unique instance.
     */
    public static ConfigHandler getInstance() {
        if(_instance == null)
            _instance = new ConfigHandler();
        return _instance;
    }
    
    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements
     * @param event the event from the init methods
     */
    @SuppressWarnings("unchecked")
    public void handle(FMLPreInitializationEvent event) {
        // You will be able to find the config file in .minecraft/config/ and it will be named EvilCraft.cfg
        // here our Configuration has been instantiated, and saved under the name "config"
        // If the file doesn't already exist, it will be created.
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        CommandConfigSet.CONFIGURATION = config;
        
        // Loading the configuration from its file
        config.load();
        
        for(ExtendedConfig<?> eConfig : this) {
            if(!eConfig.isHardDisabled()) {
                // Save additional properties
                for(ConfigProperty configProperty : eConfig.configProperties) {
                    configProperty.save(config);
                    if(configProperty.isCommandable())
                        CommandConfig.PROPERTIES.put(configProperty.getName(), configProperty);
                }
                
                // Check the type of the element
                ElementType type = eConfig.getHolderType();
                
                // Register the element depending on the type and set the creative tab
                type.getElementTypeAction().commonRun(eConfig, config);
                
                // Call the listener
                eConfig.onRegistered();

                EvilCraft.log("Registered "+eConfig.NAMEDID);
            }
        }
        
        // Empty the configs so they won't be loaded again later
        this.removeAll(this);
        
        // Saving the configuration to its file
        config.save();
    }
    
}
