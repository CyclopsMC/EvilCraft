package evilcraft.api.config;

/**
 * An action that can be used to register Configurables
 * @author Ruben Taelman
 *
 */
public abstract class ElementTypeAction {
    public void commonRun(ExtendedConfig eConfig) {
        run(eConfig);
        
        // Optional common stuff
    }
    
    public abstract void run(ExtendedConfig eConfig);
}
