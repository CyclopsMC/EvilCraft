package evilcraft;

import evilcraft.api.config.DummyConfig;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {
    
    /**
     * The current mod version, will be used to check if the player's config isn't out of date and
     * warn the player accordingly.
     */
    @ConfigurableProperty(category = ElementTypeCategory.CORE, comment = "Config version for " + Reference.MOD_NAME +".\nDO NOT EDIT MANUALLY!")
    public static String version = Reference.MOD_VERSION;
    
    /**
     * If the debug mode should be enabled. @see Debug
     */
    @ConfigurableProperty(category = ElementTypeCategory.CORE, comment = "Set 'true' to enable debug mode.\nThis will result in a lower performance!")
    public static boolean debug = false;
    
    /**
     * If players are able to die without any reason.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "Evil stuff...", isCommandable = true)
    public static boolean dieWithoutAnyReason = false;
    
    /**
     * The type of this config.
     */
    public static ElementType TYPE = ElementType.DUMMY;
    
    /**
     * If silverfish should spawn in all biomes.
     */
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "Spawn extra netherfish blocks in all biomes.")
    public static boolean extraSilverfish = false;
    /**
     * The amount of blocks per vein there should be.
     */
    public static int silverfish_BlocksPerVein = 4;
    /**
     * The amount of veins per chunk there should be.
     */
    public static int silverfish_VeinsPerChunk = 10;
    /**
     * The Y start value for generation to start (lowest Y value).
     */
    public static int silverfish_StartY = 6;
    /**
     * The Y end value for generation to end (larget Y value).
     */
    public static int silverfish_EndY = 66;
    
    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(0, "General Info", "info", null, GeneralConfig.class);
    }
    
    @Override
    public void onRegistered() {
        // Check version of config file
        if(!version.equals(Reference.MOD_VERSION))
            System.err.println("The config file of " + Reference.MOD_NAME + " is out of date and might cause problems, please remove it so it can be regenerated.");
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
