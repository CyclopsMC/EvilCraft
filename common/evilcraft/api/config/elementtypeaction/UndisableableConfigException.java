package evilcraft.api.config.elementtypeaction;

import evilcraft.api.config.ExtendedConfig;

/**
 * An exception that is thrown when the player disabled an undisableable config.
 * @author Ruben Taelman
 *
 */
public class UndisableableConfigException extends RuntimeException {
    
    public UndisableableConfigException(ExtendedConfig eConfig) {
        super("The configuration for "+eConfig.NAME+" was disabled in the config file, please enable it back since this mod can't function without it.");
    }
}
