package evilcraft.core;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

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
                    log(config.getElement()+" has no sub-instance, even though it is enabled.");
                }
                
                // getInstance() in the sub-instance of ExtendedConfig
                try {
                    config.getElement().getMethod("getInstance");
                } catch (NoSuchMethodException e) {
                    log(config.getElement()+" has no static 'getInstance()' method.");
                } catch (SecurityException e) {
                    log(config.getElement()+" has a non-public static 'getInstance()' method, make it public.");
                }
            }
        }
        
        if(ok) {
            EvilCraft.clog(CONFIGCHECKER_PREFIX + "Everything is just fine!");
        }
    }
    
    private static void log(String message) {
        ok = false;
        EvilCraft.clog(CONFIGCHECKER_PREFIX + message, Level.INFO);
    }

}
