package evilcraft.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;
import evilcraft.Reference;

/**
 * Based on iChun's LoggerHelper: https://github.com/iChun/iChunUtil/blob/master/ichun/core/LoggerHelper.java
 * @author rubensworks
 *
 */
public class LoggerHelper
{
    private static Logger logger = Logger.getLogger(Reference.MOD_NAME);

    public static void init()
    {
        logger.setParent(FMLLog.getLogger());
    }

    public static void log(Level logLevel, String message)
    {
        logger.log(logLevel, message);
    }

    public static Logger createLogger(String s)
    {
        Logger logger = Logger.getLogger(s);
        logger.setParent(FMLLog.getLogger());
        return logger;
    }
}
