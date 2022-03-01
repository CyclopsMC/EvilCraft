package org.cyclops.evilcraft;

import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;


/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {

    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    @ConfigurableProperty(category = "general", comment = "Evil stuff...", isCommandable = true, configLocation = ModConfig.Type.SERVER)
    public static boolean dieWithoutAnyReason = false;

    @ConfigurableProperty(category = "general", comment = "If farting is enabled on this server; Client-side: If farting can be seen at your client.", isCommandable = true, configLocation = ModConfig.Type.SERVER)
    public static boolean farting = true;

    /*@ConfigurableProperty(category = "fluid",
            comment = "The allowed blood conversions with their ratio. (ratio 2 means that this "
                    + "fluid is 1mB of this fluid can be converted into 2mB of EvilCraft Blood.",
            configLocation = ModConfig.Type.SERVER)
    public static List<String> bloodConverters = Lists.newArrayList(
            "blood:1.0",
            "lifeessence:1.0",
            "hell_blood:1.0"
    );*/

    @ConfigurableProperty(category = "general", comment = "The amount of mB that can flow per tick out of machines and items.", requiresMcRestart = true, configLocation = ModConfig.Type.SERVER)
    public static int mbFlowRate = 100;

    @ConfigurableProperty(category = "worldgeneration", comment = "Spawn extra silverfish blocks in all biomes.", configLocation = ModConfig.Type.SERVER)
    public static boolean extraSilverfish = false;

    @ConfigurableProperty(category = "general", comment = "If the blood gui overlay should be rendered.", isCommandable = true, configLocation = ModConfig.Type.CLIENT)
    public static boolean bloodGuiOverlay = true;
    @ConfigurableProperty(category = "general", comment = "The position to render the blood gui overlay at. (0=NE, 1=SE, 2=SW,3=NW)", isCommandable = true, configLocation = ModConfig.Type.CLIENT)
    public static int bloodGuiOverlayPosition = 1;
    @ConfigurableProperty(category = "general", comment = "The X offset for the blood gui overlay.", isCommandable = true, configLocation = ModConfig.Type.CLIENT)
    public static int bloodGuiOverlayPositionOffsetX = -5;
    @ConfigurableProperty(category = "general", comment = "The Y offset for the blood gui overlay.", isCommandable = true, configLocation = ModConfig.Type.CLIENT)
    public static int bloodGuiOverlayPositionOffsetY = -5;

    @ConfigurableProperty(category = "worldgeneration", comment = "The spawn chance for loot chests in dark temples, set to zero to completely disable.", isCommandable = true, requiresMcRestart = true, configLocation = ModConfig.Type.SERVER)
    public static double darkTempleChestChance = 0.15D;

    public GeneralConfig() {
        super(EvilCraft._instance, "general");
    }

    @Override
    public void onRegistered() {
        if(analytics) {
            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod(getMod(), EvilCraft._instance, Reference.VERSION_URL);
        }
    }
}
