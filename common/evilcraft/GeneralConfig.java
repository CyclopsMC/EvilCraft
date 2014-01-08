package evilcraft;

import evilcraft.api.config.DummyConfig;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;

public class GeneralConfig extends DummyConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.CORE, comment = "Config version for " + Reference.MOD_NAME +".\nDO NOT EDIT MANUALLY!")
    public static String version = Reference.MOD_VERSION;
    
    @ConfigurableProperty(category = ElementTypeCategory.CORE, comment = "Set 'true' to enable debug mode.\nThis will result in a lower performance!")
    public static boolean debug = false;
    
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "Evil stuff...", isCommandable = true)
    public static boolean dieWithoutAnyReason = false;
    
    public static ElementType TYPE = ElementType.DUMMY;
    
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "Spawn extra netherfish blocks in all biomes.")
    public static boolean extraSilverfish = false;
    public static int silverfish_BlocksPerVein = 4;
    public static int silverfish_VeinsPerChunk = 10;
    public static int silverfish_StartY = 6;
    public static int silverfish_EndY = 66;
    
    public GeneralConfig() {
        super(0, "General Info", "info", null, GeneralConfig.class);
    }
    
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
