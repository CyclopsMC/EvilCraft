package evilcraft.api;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;

import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;

/**
 * Helps with code debugging.
 * @author rubensworks
 *
 */
public class Debug {
    
    private static String CONFIGCHECKER_PREFIX = "[CONFIGCHECKER] ";
    @SuppressWarnings("rawtypes")
    private static Set<ExtendedConfig> savedConfigs = new HashSet<ExtendedConfig>();
    
    private static boolean ok = true;

    /**
     * Loops over the list of configs and checks their correctness.
     * @param configs List of configs
     */
    @SuppressWarnings("rawtypes")
    public static void checkPreConfigurables(Set<ExtendedConfig> configs) {
        for(ExtendedConfig config : configs) {
            // _instance field on ExtendedConfig
            try {
                config.getClass().getField("_instance");
            } catch (NoSuchFieldException e) {
                log(config+" has no static '_instance' field.");
            } catch (SecurityException e) {
                log(config+" has a non-public static '_instance' field, make it public.");
            }
        }
        
        // Save for Post call
        savedConfigs.addAll(configs);
    }
    
    /**
     * Loops over the list of configs (was saved from the Pre call) and checks their correctness.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void checkPostConfigurables() {
        for(ExtendedConfig config : savedConfigs) {            
            if(config.getHolderType().hasUniqueInstance() && config.isEnabled()) {
                // The sub-instance of ExtendedConfig (can be in a higher class hierarchy, bit of a hack...
                if(config.getSubInstance() == null) {
                    log(config.ELEMENT+" has no sub-instance, even though it is enabled.");
                }
                
                // initInstance(ExtendedConfig eConfig) in the sub-instance of ExtendedConfig
                try {
                    config.ELEMENT.getMethod("initInstance", ExtendedConfig.class);
                } catch (NoSuchMethodException e) {
                    log(config.ELEMENT+" has no static 'initInstance(ExtendedConfig eConfig)' method.");
                } catch (SecurityException e) {
                    log(config.ELEMENT+" has a non-public static 'initInstance(ExtendedConfig eConfig)' method, make it public.");
                }
                
                // getInstance() in the sub-instance of ExtendedConfig
                try {
                    config.ELEMENT.getMethod("getInstance");
                } catch (NoSuchMethodException e) {
                    log(config.ELEMENT+" has no static 'getInstance()' method.");
                } catch (SecurityException e) {
                    log(config.ELEMENT+" has a non-public static 'getInstance()' method, make it public.");
                }
            }
        }
        
        if(ok) {
            EvilCraft.log(CONFIGCHECKER_PREFIX+"Everything is just fine!");
        }
    }
    
    private static void log(String message) {
        ok = false;
        EvilCraft.log(CONFIGCHECKER_PREFIX+message, Level.INFO);
    }

}
