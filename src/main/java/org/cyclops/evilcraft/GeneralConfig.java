package org.cyclops.evilcraft;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "Config version for " + Reference.MOD_NAME +".\nDO NOT EDIT MANUALLY!")
    public static String version = Reference.MOD_VERSION;
    
    /**
     * If the debug mode should be enabled. @see Debug
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "Set 'true' to enable development debug mode. This will result in a lower performance!", requiresMcRestart = true)
    public static boolean debug = false;

    /**
     * If the recipe loader should crash when finding invalid recipes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true)
    public static boolean crashOnInvalidRecipe = true;

    /**
     * If mod compatibility loader should crash hard if errors occur in that process.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true)
    public static boolean crashOnModCompatCrash = false;

    /**
     * If an anonymous mod startup analytics request may be sent to our analytics service.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;
    
    /**
     * If players are able to die without any reason.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "Evil stuff...", isCommandable = true)
    public static boolean dieWithoutAnyReason = false;
    
    /**
     * If the version checker should be enabled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;
    
    /**
     * Server-side: If farting is enabled on this server; Client-side: If farting can be seen at your client.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "Server-side: If farting is enabled on this server; Client-side: If farting can be seen at your client.", isCommandable = true)
    public static boolean farting = true;
    
    /**
     * The allowed blood conversions with their ratio.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.FLUID,
    		comment = "The allowed blood conversions with their ratio. (ratio 2 means that this "
    				+ "fluid is 1mB of this fluid can be converted into 2mB of EvilCraft Blood.",
    		changedCallback = BloodFluidConverter.BloodConvertersChanged.class)
    public static String[] bloodConverters = new String[]{
    	"blood:1.0",
    	"lifeessence:1.0",
        "hell_blood:1.0",
    };
    
    /**
     * If retro-generation of ores should be enabled. WARNING: This could cause lag if permanently enabled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If retro-generation of ores should be enabled. WARNING: This could cause lag if permanently enabled.", isCommandable = true)
    public static boolean retrogen = false;
    
    /**
     * The type of this config.
     */
    public static ConfigurableType TYPE = ConfigurableType.DUMMY;
    
    /**
     * If silverfish should spawn in all biomes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "Spawn extra netherfish blocks in all biomes.")
    public static boolean extraSilverfish = false;
    
    /**
     * Minimum height at which a dark temple can spawn.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "Minimum block height at which a dark temple can spawn.")
    public static int darkTempleMinHeight = 60;
    
    /**
     * Maximum height at which a dark temple can spawn.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "Maximum blockState height at which a dark temple can spawn.")
    public static int darkTempleMaxHeight = 256;
    
    /**
     * Frequency at which dark temples should occur. 1 means every chunk, 100 means every 100th chunk.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "Frequency at which dark temples should occur. 1 means every chunk, 100 means every 100th chunk.")
    public static int darkTempleFrequency = 250;

    /**
     * Maximal length of the pillars of a dark temple.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "Maximal length of the pillars of a dark temple.")
    public static int darkTempleMaxPillarLength = 20;

    /**
     * Whitelisted dimensions in which dark temples can generate.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "Whitelisted dimensions in which dark temples can generate.")
    public static int[] darkTempleDimensions = new int[]{
            0
    };

    /**
     * The amount of mB that can flow per tick.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "The amount of mB that can flow per tick out of machines and items.", requiresMcRestart = true)
    public static int mbFlowRate = 100;

    /**
     * The minimum array size of potion types, increase to allow for more potion types.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "The minimum array size of potion types, increase to allow for more potion types.", requiresMcRestart = true)
    public static int minimumPotionTypesArraySize = 256;

    /**
     * [Thaumcraft] If temporary warp should be added when hit by a Vengeance Spirit.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "[Thaumcraft] If temporary warp should be added when hit by a Vengeance Spirit.", requiresMcRestart = true)
    public static boolean thaumcraftVengeanceSpiritWarp = true;

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
        super(EvilCraft._instance, true, "general", null, GeneralConfig.class);
    }
    
    @Override
    public void onRegistered() {
        // Check version of config file
        if(!version.equals(Reference.MOD_VERSION))
            System.err.println("The config file of " + Reference.MOD_NAME + " is out of date and might cause problems, please remove it so it can be regenerated.");

        getMod().putGenericReference(ModBase.REFKEY_RETROGEN, GeneralConfig.retrogen);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
        getMod().putGenericReference(ModBase.REFKEY_DEBUGCONFIG, GeneralConfig.debug);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH, GeneralConfig.crashOnModCompatCrash);

        if(analytics) {
            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod(getMod(), EvilCraft._instance, Reference.VERSION_URL);
        }
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
