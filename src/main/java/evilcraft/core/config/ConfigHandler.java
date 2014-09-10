package evilcraft.core.config;

import java.util.LinkedHashSet;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import evilcraft.EvilCraft;
import evilcraft.IInitListener;
import evilcraft.command.CommandConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;

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
    
    private Configuration config;
    private LinkedHashSet<ExtendedConfig> processedConfigs = new LinkedHashSet<ExtendedConfig>();
    private Map<String, ExtendedConfig> configDictionary = Maps.newHashMap();
    
    /**
     * Get the unique instance.
     * @return Unique instance.
     */
    public static ConfigHandler getInstance() {
        if(_instance == null)
            _instance = new ConfigHandler();
        return _instance;
    }
    
    @Override
    public boolean add(ExtendedConfig e) {
    	configDictionary.put(e.getNamedId(), e);
    	return super.add(e);
    }
    
    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements
     * This also sets the config of this instance.
     * @param event the event from the init methods
     */
    public void handle(FMLPreInitializationEvent event) {
    	 // You will be able to find the config file in .minecraft/config/ and it will be named EvilCraft.cfg
        // here our Configuration has been instantiated, and saved under the name "config"
        // If the file doesn't already exist, it will be created.
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        setConfig(config);
        
        // Loading the configuration from its file
        config.load();
        
        loadConfig();
    }
    
    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements.
     */
    @SuppressWarnings("unchecked")
    public void loadConfig() {
        for(ExtendedConfig<?> eConfig : this) {
            if(!eConfig.isHardDisabled()) {
                // Save additional properties
                for(ConfigProperty configProperty : eConfig.configProperties) {
                    configProperty.save(config);
                    if(configProperty.isCommandable())
                        CommandConfig.PROPERTIES.put(configProperty.getName(), configProperty);
                }
                
                // Register the element depending on the type.
                ConfigurableType type = eConfig.getHolderType();
                type.getElementTypeAction().commonRun(eConfig, config);
                
                if(eConfig.isEnabled()) {
	                // Call the listener
	                eConfig.onRegistered();
	
	                EvilCraft.log("Registered "+eConfig.getNamedId());
	                processedConfigs.add(eConfig);
	                
	                // Register as init listener.
	                EvilCraft.addInitListeners(new ConfigInitListener(eConfig));
                }
            }
        }
        
        // Empty the configs so they won't be loaded again later
        this.removeAll(this);
        
        // Saving the configuration to its file
        config.save();
    }
    
    /**
     * Sync the config values that were already loaded.
     * This will update the values in-game and in the config file.
     */
    @SuppressWarnings("unchecked")
	public void syncProcessedConfigs() {
    	for(ExtendedConfig<?> eConfig : processedConfigs) {
    		// Re-save additional properties
            for(ConfigProperty configProperty : eConfig.configProperties) {
                configProperty.save(config, false);
            }
            
            // Register the element depending on the type.
            ConfigurableType type = eConfig.getHolderType();
            type.getElementTypeAction().preRun(eConfig, config, false);
            
            // Update the config file.
            getConfig().save();
    	}
    }

	/**
	 * @return the config
	 */
	public Configuration getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Configuration config) {
		this.config = config;
	}
	
	/**
	 * Get the map of config nameid to config.
	 * @return The dictionary.
	 */
	public Map<String, ExtendedConfig> getDictionary() {
		return configDictionary;
	}
	
	/**
	 * Init listener for configs.
	 * @author rubensworks
	 *
	 */
	public static class ConfigInitListener implements IInitListener {

		private ExtendedConfig<?> config;
		
		/**
		 * Make a new instance.
		 * @param config The config.
		 */
		public ConfigInitListener(ExtendedConfig<?> config) {
			this.config = config;
		}
		
		@Override
		public void onInit(IInitListener.Step step) {
			config.onInit(step);
			if(step == IInitListener.Step.POSTINIT) {
				for(ConfigProperty property : config.configProperties) {
					IChangedCallback changedCallback = property.getCallback().getChangedCallback();
					if(changedCallback != null) {
						changedCallback.onRegisteredPostInit(property.getValue());
					}
				}
			}
		}
		
	}
    
}
