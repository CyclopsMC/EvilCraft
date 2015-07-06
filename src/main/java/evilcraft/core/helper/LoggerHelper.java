package evilcraft.core.helper;

import evilcraft.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger that will be used in this mod.
 * @author rubensworks
 *
 */
public class LoggerHelper {
	
    private static Logger logger = LogManager.getLogger(Reference.MOD_NAME);

    /**
     * Initialize the logger.
     */
    public static void init() { }
    
    /**
     * Log a new message.
     * @param logLevel The level to clog at.
     * @param message The message to clog.
     */
    public static void log(Level logLevel, String message) {
        logger.log(logLevel, message);
    }
}
