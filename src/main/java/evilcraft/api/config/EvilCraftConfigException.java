package evilcraft.api.config;

/**
 * Exceptions that can occur when configuring this mod.
 * @author rubensworks
 *
 */
public class EvilCraftConfigException extends RuntimeException {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Make a new instance.
     * @param message The message.
     */
    public EvilCraftConfigException(String message) {
        super(message);
    }
    
}
