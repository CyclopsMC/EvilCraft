package evilcraft.api.config.elementtypeaction;

import evilcraft.api.config.EvilCraftConfigException;
import evilcraft.api.config.ExtendedConfig;

/**
 * An exception that is thrown when the player disabled an undisableable config.
 * @author rubensworks
 *
 */
public class UndisableableConfigException extends EvilCraftConfigException {
    
    /**
     * The serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Make a new instance of the exception.
     * @param eConfig The config that caused the exception and was thus disabled.
     */
    @SuppressWarnings("rawtypes")
    public UndisableableConfigException(ExtendedConfig eConfig) {
        super("The configuration for "+eConfig.NAMEDID+" was disabled in the config file, please enable it back since this mod can't function without it.");
    }
}
