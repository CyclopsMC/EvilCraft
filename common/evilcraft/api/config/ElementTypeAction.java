package evilcraft.api.config;

import net.minecraftforge.common.Configuration;

/**
 * An action that can be used to register Configurables
 * @author Ruben Taelman
 *
 */
public abstract class ElementTypeAction {
    public void commonRun(ExtendedConfig eConfig, Configuration config) {
        run(eConfig, config);
        
        // Optional common stuff
    }
    
    public abstract void run(ExtendedConfig eConfig, Configuration config);
}
